package org.moskito.control.plugins.mattermost.api.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.moskito.control.plugins.mattermost.api.BaseResponse;

/**
 * Class for get channel by name response.
 * See for more details and fields description:
 *  https://api.mattermost.com/#tag/channels%2Fpaths%2F~1teams~1%7Bteam_id%7D~1channels~1name~1%7Bchannel_name%7D%2Fget
 */
public class GetChannelByNameResponse extends BaseResponse {

    private String id;
    @JsonProperty("create_at")
    private long createAt;
    @JsonProperty("update_at")
    private long updateAt;
    @JsonProperty("delete_at")
    private long deleteAt;
    @JsonProperty("team_id")
    private String teamId;
    private String type;
    @JsonProperty("display_name")
    private String displayName;
    private String name;
    private String header;
    private String purpose;
    @JsonProperty("last_post_at")
    private long lastPostAt;
    @JsonProperty("total_msg_count")
    private long totalMsgCount;
    @JsonProperty("extra_update_at")
    private long extraUpdateAt;
    @JsonProperty("creator_id")
    private String creatorId;

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

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public long getLastPostAt() {
        return lastPostAt;
    }

    public void setLastPostAt(long lastPostAt) {
        this.lastPostAt = lastPostAt;
    }

    public long getTotalMsgCount() {
        return totalMsgCount;
    }

    public void setTotalMsgCount(long totalMsgCount) {
        this.totalMsgCount = totalMsgCount;
    }

    public long getExtraUpdateAt() {
        return extraUpdateAt;
    }

    public void setExtraUpdateAt(long extraUpdateAt) {
        this.extraUpdateAt = extraUpdateAt;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

}
