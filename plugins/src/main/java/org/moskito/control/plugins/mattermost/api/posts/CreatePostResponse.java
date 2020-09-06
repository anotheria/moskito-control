package org.moskito.control.plugins.mattermost.api.posts;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.moskito.control.plugins.mattermost.api.BaseResponse;

/**
 * Class for response of create post request.
 *
 * See for more details and fields description:
 *  https://api.mattermost.com/#tag/posts%2Fpaths%2F~1teams~1%7Bteam_id%7D~1channels~1%7Bchannel_id%7D~1posts~1create%2Fpost
 */
public class CreatePostResponse extends BaseResponse {

    private String id;
    @JsonProperty("create_at")
    private long createAt;
    @JsonProperty("update_at")
    private long updateAt;
    @JsonProperty("delete_at")
    private long deleteAt;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("channel_id")
    private String channelId;
    @JsonProperty("root_id")
    private String rootId;
    @JsonProperty("parent_id")
    private String parentId;
    @JsonProperty("original_id")
    private String originalId;
    private String message;
    private String type;
    private String hashtag;
    @JsonProperty("pending_post_id")
    private String pendingPostId;
    @JsonProperty("is_pinned")
    private boolean isPinned;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(long updateAt) {
        this.updateAt = updateAt;
    }

    public long getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(long deleteAt) {
        this.deleteAt = deleteAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOriginalId() {
        return originalId;
    }

    public void setOriginalId(String originalId) {
        this.originalId = originalId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
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
