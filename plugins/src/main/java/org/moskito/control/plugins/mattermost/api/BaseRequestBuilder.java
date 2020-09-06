package org.moskito.control.plugins.mattermost.api;

/**
 * Basic builder for requests.
 * Contains method to determinate team and channel ids by it`s names.
 */
public abstract class BaseRequestBuilder {

    /**
     * Mattermost API wrapper main object.
     * Required for finding teams and channels ids
     */
    private MattermostApi api;

    /**
     * Initialize api field
     * @param api Mattermost api main class object
     */
    protected BaseRequestBuilder(MattermostApi api) {
        this.api = api;
    }

    /**
     * Return team id storage instance of current mattermost session.
     * Used in child classes for replacing team names to ids
     * @return team id storage instance
     */
    protected TeamIdStorage getTeamIdStorage(){
        return api.getTeamIdStorage();
    }

    /**
     * Return channel id storage instance of current mattermost session
     * Used in child classes for replacing channel names to ids
     * @return channel id storage instance
     */
    protected ChannelIdStorage getChannelIdStorage(String teamName){
        return api.getChannelIdStorage(teamName);
    }

}
