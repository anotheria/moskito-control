package org.moskito.control.plugins.mattermost.api.channels;

import org.moskito.control.plugins.mattermost.api.BaseGetRequest;
import org.moskito.control.plugins.mattermost.api.MattermostApi;

/**
 * Get channel by name request class.
 * See for more details:
 *   https://api.mattermost.com/#tag/channels%2Fpaths%2F~1teams~1%7Bteam_id%7D~1channels~1name~1%7Bchannel_name%7D%2Fget
 */
public class GetChannelByNameRequest extends BaseGetRequest<GetChannelByNameResponse> {

    /**
     * Name of channel to get
     */
    private String channelName;

    /**
     * Id of team, where channel located
     */
    private String teamId;

    public GetChannelByNameRequest(MattermostApi api) {
        super(GetChannelByNameResponse.class, api);
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

}
