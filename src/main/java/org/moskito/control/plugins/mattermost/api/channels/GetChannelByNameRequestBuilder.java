package org.moskito.control.plugins.mattermost.api.channels;

import org.moskito.control.plugins.mattermost.api.BaseRequestBuilder;
import org.moskito.control.plugins.mattermost.api.MattermostApi;
import org.moskito.control.plugins.mattermost.api.exceptions.MattermostAPIException;
import org.moskito.control.plugins.mattermost.api.exceptions.MattermostAPIInternalException;

import java.io.IOException;

/**
 * Builder for get channel by name request.
 * Replaces team name parameter with team id
 * taking it from team id storage.
 */
public class GetChannelByNameRequestBuilder extends BaseRequestBuilder {

    private String teamName;

    public GetChannelByNameRequestBuilder(MattermostApi api) {
        super(api);
        request = new GetChannelByNameRequest(api);
    }

    public GetChannelByNameRequestBuilder setChannelName(String channelName) {
        request.setChannelName(channelName);
        return this;
    }

    public GetChannelByNameRequestBuilder setTeamName(String teamName) {
        this.teamName = teamName;
        return this;
    }

    private GetChannelByNameRequest request;

    /**
     * @return get channel by name request object
     * @throws MattermostAPIInternalException on get channel or team id request fail
     * @throws IOException on get channel or team id request fail
     * @throws MattermostAPIException on get channel or team id request fail
     */
    public GetChannelByNameRequest build() throws MattermostAPIInternalException, IOException, MattermostAPIException {
        request.setTeamId(
                getTeamIdStorage().getTeamIdByName(teamName)
        );
        return request;
    }

}
