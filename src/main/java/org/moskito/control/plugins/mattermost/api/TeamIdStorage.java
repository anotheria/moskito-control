package org.moskito.control.plugins.mattermost.api;

import org.moskito.control.plugins.mattermost.api.exceptions.MattermostAPIException;
import org.moskito.control.plugins.mattermost.api.exceptions.MattermostAPIInternalException;
import org.moskito.control.plugins.mattermost.api.teams.GetTeamObjectRequestBuilder;
import org.moskito.control.plugins.mattermost.api.teams.GetTeamObjectResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Storage for team name and id aliases.
 */
public class TeamIdStorage {

    /**
     * Map, where team name is a key and team id is a value.
     */
    private Map<String, String> teamNameAndIdAlias = new HashMap<>();
    /**
     * link to main api wrapper class,
     * that contains this storage.
     * Required to make request for obtaining team id by name
     */
    private MattermostApi api;

    /**
     * Constructor, that initialize api field
     * @param api Mattermost API wrapper main class object
     */
    TeamIdStorage(MattermostApi api) {
        this.api = api;
    }

    /**
     * Returns team id by its name.
     * Makes request to Mattermost API and saves result for further usage.
     * @param teamName name of team to get id
     * @return id of team with name specified in method argument
     */
    public String getTeamIdByName(String teamName) throws MattermostAPIInternalException, IOException, MattermostAPIException {

        // Filling alias with this team by request to API if it not present
        if(!teamNameAndIdAlias.containsKey(teamName)){
            GetTeamObjectResponse getTeamObjectResponse =
                    api.getTeamObject(
                            new GetTeamObjectRequestBuilder(api)
                                    .setTeamName(teamName)
                                    .build()
                    );
            teamNameAndIdAlias.put(teamName, getTeamObjectResponse.getId());
        }

        return teamNameAndIdAlias.get(teamName);

    }

}
