package org.moskito.control.plugins.mattermost.api.teams;

import org.moskito.control.plugins.mattermost.api.BaseRequestBuilder;
import org.moskito.control.plugins.mattermost.api.MattermostApi;

/**
 * Builder for get team object by name request
 */
public class GetTeamObjectRequestBuilder extends BaseRequestBuilder{

    private GetTeamObjectRequest request;

    public GetTeamObjectRequestBuilder(MattermostApi api) {
        super(api);
        request = new GetTeamObjectRequest(api);
    }

    public GetTeamObjectRequestBuilder setTeamName(String teamName){
        request.setTeamName(teamName);
        return this;
    }

    public GetTeamObjectRequest build(){
        return request;
    }

}
