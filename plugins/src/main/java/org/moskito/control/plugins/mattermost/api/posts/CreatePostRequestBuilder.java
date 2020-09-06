package org.moskito.control.plugins.mattermost.api.posts;

import org.moskito.control.plugins.mattermost.api.BaseRequestBuilder;
import org.moskito.control.plugins.mattermost.api.MattermostApi;
import org.moskito.control.plugins.mattermost.api.exceptions.MattermostAPIException;

import java.io.IOException;

/**
 * Builder for create post request
 * Replaces channel and team names with corresponding id
 * using team and channel storages
 */
public class CreatePostRequestBuilder extends BaseRequestBuilder{

    private String channelName;
    private String teamName;

    public CreatePostRequestBuilder setTeamName(String teamName){
        this.teamName = teamName;
        return this;
    }

    public CreatePostRequestBuilder setChannelName(String channelName){
        this.channelName = channelName;
        return this;
    }

    public CreatePostRequestBuilder setId(String id) {
        request.setId(id);
        return this;
    }

    public CreatePostRequestBuilder setCreateAt(int createAt) {
        request.setCreateAt(createAt);
        return this;
    }

    public CreatePostRequestBuilder setDeleteAt(int deleteAt) {
        request.setDeleteAt(deleteAt);
        return this;
    }

    public CreatePostRequestBuilder setUserId(String userId) {
        request.setUserId(userId);
        return this;
    }

    public CreatePostRequestBuilder setRootId(String rootId) {
        request.setRootId(rootId);
        return this;
    }

    public CreatePostRequestBuilder setParentId(String parentId) {
        request.setParentId(parentId);
        return this;
    }

    public CreatePostRequestBuilder setOriginalId(String originalId) {
        request.setOriginalId(originalId);
        return this;
    }

    public CreatePostRequestBuilder setMessage(String message) {
        request.setMessage(message);
        return this;
    }

    public CreatePostRequestBuilder setType(String type) {
        request.setType(type);
        return this;
    }

    public CreatePostRequestBuilder setHashtag(String hashtag) {
        request.setHashtag(hashtag);
        return this;
    }

    private CreatePostRequest request;

    public CreatePostRequestBuilder(MattermostApi api) {
        super(api);
        request = new CreatePostRequest(api);
    }

    /**
     * Builds request object.
     * @return Create post request object
     * @throws ReflectiveOperationException on bugs in request/response objects
     * @throws IOException on get channel or team id request fail
     * @throws MattermostAPIException on get channel or team id request fail
     */
    public CreatePostRequest build() throws ReflectiveOperationException, IOException, MattermostAPIException {

        if(teamName != null)
            request.setTeamId(
                    getTeamIdStorage().getTeamIdByName(teamName)
            );

        if(teamName != null && channelName != null)
            request.setChannelId(
                    getChannelIdStorage(teamName).getChannelIdByName(channelName)
            );

        return request;

    }

}
