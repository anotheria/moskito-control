package org.moskito.control.plugins.mattermost.api;

import org.moskito.control.plugins.mattermost.api.channels.GetChannelByNameRequestBuilder;
import org.moskito.control.plugins.mattermost.api.channels.GetChannelByNameResponse;
import org.moskito.control.plugins.mattermost.api.exceptions.MattermostAPIException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Storage for channel name and id aliases for specified team
 */
public class ChannelIdStorage {

    /**
     * Map, where channel name is a key and channel id is a value
     */
    private Map<String, String> channelNameAndIdAlias = new HashMap<>();
    /**
     * link to main api wrapper class,
     * that contains this storage.
     * Required to make request for obtaining team id by name
     */
    private MattermostApi api;
    /**
     * Name of team, where channels of this storage is located
     */
    private String teamName;

    /**
     * Constructor, that initialize main Mattermost API wrapper class
     * and team name.
     * @param api object of main Mattermost API wrapper class
     * @param teamName name of a team, where channels of this storage located
     */
    ChannelIdStorage(MattermostApi api, String teamName) {
        this.api = api;
        this.teamName = teamName;
    }

    /**
     * Returns team name of this storage
     * @return team name
     */
    String getTeamName(){
        return teamName;
    }

    /**
     * Returns channel id by it`s name.
     * @param channelName name of channel to retrieve id
     * @return channel id string
     */
    public String getChannelIdByName(String channelName) throws ReflectiveOperationException, IOException, MattermostAPIException {
        // Make request to Mattermost API if channel alias is not present
        if(!channelNameAndIdAlias.containsKey(channelName)){

            GetChannelByNameResponse getChannelByNameResponse =
                    api.getChannelByName(
                            new GetChannelByNameRequestBuilder(api)
                                    .setTeamName(teamName)
                                    .setChannelName(channelName)
                                    .build()
                    );
            channelNameAndIdAlias.put(channelName, getChannelByNameResponse.getId());

        }

        return channelNameAndIdAlias.get(channelName);
    }


}
