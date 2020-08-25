package org.moskito.control.plugins.mattermost.api.teams;

import org.moskito.control.plugins.mattermost.api.BaseGetRequest;
import org.moskito.control.plugins.mattermost.api.MattermostApi;

/**
 * Request to get team object.
 * Mostly used to retrieve team id by it name.
 */
public class GetTeamObjectRequest extends BaseGetRequest<GetTeamObjectResponse> {

    /**
     * Name of team to get team object
     */
    private String teamName;

    GetTeamObjectRequest(MattermostApi api) {
        super(GetTeamObjectResponse.class, api);
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

}
