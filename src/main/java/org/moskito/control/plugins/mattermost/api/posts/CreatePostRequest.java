package org.moskito.control.plugins.mattermost.api.posts;

import org.moskito.control.plugins.mattermost.api.BasePostRequest;
import org.moskito.control.plugins.mattermost.api.MattermostApi;

/**
 * Create post request class
 * See for more details:
 *  https://api.mattermost.com/#tag/posts%2Fpaths%2F~1teams~1%7Bteam_id%7D~1channels~1%7Bchannel_id%7D~1posts~1create%2Fpost
 */
public class CreatePostRequest extends BasePostRequest<CreatePostResponse> {

    private String id;

    private long  createAt;

    private long updateAt;

    private long  deleteAt;

    private String userId;

    private String channelId;

    private String teamId;

    private String rootId;

    private String parentId;

    private String originalId;

    private String message;

    private String type;

    private String hashtag;

    private String pendingPostId;

    private boolean isPinned;

    public CreatePostRequest(MattermostApi api) {
        super(CreatePostResponse.class, api);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public void setDeleteAt(long deleteAt) {
        this.deleteAt = deleteAt;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setOriginalId(String originalId) {
        this.originalId = originalId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getId() {
        return id;
    }

    public long getCreateAt() {
        return createAt;
    }

    public long getDeleteAt() {
        return deleteAt;
    }

    public String getUserId() {
        return userId;
    }

    public String getRootId() {
        return rootId;
    }

    public String getParentId() {
        return parentId;
    }

    public String getOriginalId() {
        return originalId;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public String getHashtag() {
        return hashtag;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(long updateAt) {
        this.updateAt = updateAt;
    }

    public String getPendingPostId() {
        return pendingPostId;
    }

    public void setPendingPostId(String pendingPostId) {
        this.pendingPostId = pendingPostId;
    }

    public boolean getIsPinned() {
        return isPinned;
    }

    public void setIsPinned(boolean isPinned) {
        this.isPinned = isPinned;
    }
}
