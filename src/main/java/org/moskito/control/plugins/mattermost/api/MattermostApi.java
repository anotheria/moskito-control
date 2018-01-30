package org.moskito.control.plugins.mattermost.api;

import org.moskito.control.plugins.mattermost.api.channels.GetChannelByNameRequest;
import org.moskito.control.plugins.mattermost.api.channels.GetChannelByNameResponse;
import org.moskito.control.plugins.mattermost.api.exceptions.MattermostAPIException;
import org.moskito.control.plugins.mattermost.api.posts.CreatePostRequest;
import org.moskito.control.plugins.mattermost.api.posts.CreatePostResponse;
import org.moskito.control.plugins.mattermost.api.teams.GetTeamObjectRequest;
import org.moskito.control.plugins.mattermost.api.teams.GetTeamObjectResponse;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Mattermost API wrapper main class.
 * Contains API authorization credentials.
 * All request to API starts from here.
 *
 * Mattermost API request,that corresponds to specified teams or channels, are
 * made by using its ids. For simplifying writing such requests
 * this wrapper contains mechanism of replacing team or channel names to it ids.
 * So that class also contains storages for team and channel name with id aliases.
 */
public class MattermostApi {

    /**
     * Token of Mattermost user.
     * Retrieves after auth request,
     * Witch made in constructor of this class
     */
    private String token;
    /**
     * Host, where mattermost instance located
     */
    private String host;

    /**
     * Alias for team names and ids
     */
    private TeamIdStorage teamIdStorage;
    /**
     * Alias for channel names and ids
     * Channel alias storages divided by teams.
     * This list is empty by default, storages objects for teams created on demand
     */
    private List<ChannelIdStorage> channelIdStorages = new LinkedList<>();

    /**
     * Builds full url to concrete mattermost instance (configured by constructor parameter)
     * form method url.
     * Example:
     *  "teams/name/$team_name" => "www.your_host.com/api/v3/teams/name/$team_name"
     * @param methodName name of Mattermost API method
     * @return full API method url
     */
    private String getMethodUrl(String methodName){
        return host + "/api/v3/" + methodName;
    }

    /**
     * Initialize Mattermost API.
     * Makes auth request to mattermost.
     * Saves host to object.
     * @param host mattermost instance url
     * @param username username of mattermost user
     * @param password password of mattermost user
     */
    public MattermostApi(String host, String username, String password)
            throws ReflectiveOperationException, IOException, MattermostAPIException {
        // Allows specify host with slash on end of string or without it
        this.host = host.charAt(host.length() - 1) == '/' ?
                host.substring(0, host.charAt(host.length() - 1)) : host;
        token = auth(username, password);
        teamIdStorage = new TeamIdStorage(this);

    }

    /**
     * Makes auth request to mattermost.
     *
     * See for more details:
     *      https://api.mattermost.com/#tag/authentication
     *
     * @param username login of mattermost user
     * @param password password of mattermost user
     * @return token of user, that allow do other requests
     */
    private String auth(String username, String password)
            throws ReflectiveOperationException, IOException, MattermostAPIException {

        AuthRequest request = new AuthRequest(this, username, password);

        AuthResponse response = request.makeRequest(
                getMethodUrl("users/login")
        );

        return response.getToken();

    }

    /**
     * Indicates is user successfully authorized in mattermost
     * @return true - user is authorized
     *         false - no
     */
    boolean isAuthorized(){
        return token != null;
    }

    /**
     * Triggers create post request.
     *
     * See for more details.:
     *      https://api.mattermost.com/#tag/posts%2Fpaths%2F~1teams~1%7Bteam_id%7D~1channels~1%7Bchannel_id%7D~1posts~1create%2Fpost
     *
     * @param request create post request object
     * @return response of create post request
     */
    public CreatePostResponse createPost(CreatePostRequest request)
            throws ReflectiveOperationException, IOException, MattermostAPIException {
        return request.makeRequest(
                getMethodUrl(
                        "teams/$team_id/channels/$channel_id/posts/create"
                )
        );
    }

    /**
     * Triggers get team object request.
     *
     * See for more details:
     *      https://api.mattermost.com/#tag/teams%2Fpaths%2F~1teams~1name~1%7Bteam_name%7D%2Fget
     *
     * @param request get team object request object
     * @return get team object response
     */
    public GetTeamObjectResponse getTeamObject(GetTeamObjectRequest request)
            throws ReflectiveOperationException, IOException, MattermostAPIException {
        return request.makeRequest(
                getMethodUrl("teams/name/$team_name")
        );
    }

    /**
     * Triggers get channel by name request
     *
     * See for more details:
     *      https://api.mattermost.com/#tag/channels%2Fpaths%2F~1teams~1%7Bteam_id%7D~1channels~1name~1%7Bchannel_name%7D%2Fget
     *
     * @param request get channel by name request object
     * @return get channel by name response
     */
    public GetChannelByNameResponse getChannelByName(GetChannelByNameRequest request)
            throws ReflectiveOperationException, IOException, MattermostAPIException {
        return request.makeRequest(
                getMethodUrl("teams/$team_id/channels/name/$channel_name")
        );
    }

    /**
     * Returns token of current authorized user
     * @return token string
     */
    String getToken(){
        return token;
    }

    /**
     * Return storage of team name and id aliases.
     * Used in builders
     * @return storage of team name and id aliases
     */
    TeamIdStorage getTeamIdStorage() {
        return teamIdStorage;
    }

    /**
     * Return storage of channel name and id alias for specified team.
     * Creates storage object for team, if it name first noted
     * @param teamName name of team to get channel aliases storage
     * @return channel name and id aliases storage
     */
    ChannelIdStorage getChannelIdStorage(String teamName){

        ChannelIdStorage idStorageForTeam = null;

        // Trying to get existing storage for team
        for(ChannelIdStorage idStorage : channelIdStorages) {
           if(idStorage.getTeamName().equals(teamName)) {
               idStorageForTeam = idStorage;
               break;
           }
        }

        // Create new storage object for first time noted team
        if(idStorageForTeam == null) {
            idStorageForTeam = new ChannelIdStorage(this, teamName);
            channelIdStorages.add(idStorageForTeam);
        }

        return idStorageForTeam;

    }

}
