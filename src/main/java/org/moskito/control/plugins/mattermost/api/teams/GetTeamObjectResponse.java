package org.moskito.control.plugins.mattermost.api.teams;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.moskito.control.plugins.mattermost.api.BaseResponse;

/**
 * Response for get team object request
 * See for more details and fields description:
 *  https://api.mattermost.com/#tag/teams%2Fpaths%2F~1teams~1name~1%7Bteam_name%7D%2Fget
 */
public class GetTeamObjectResponse extends BaseResponse {

    private String id;
    @JsonProperty("create_at")
    private long createAt;
    @JsonProperty("update_at")
    private long updateAt;
    @JsonProperty("delete_at")
    private long deleteAt;
    @JsonProperty("display_name")
    private String displayName;
    private String name;
    private String description;
    private String email;
    private String type;
    @JsonProperty("allowed_domains")
    private String allowedDomains;
    @JsonProperty("invite_id")
    private String inviteId;
    @JsonProperty("allow_open_invite")
    private boolean allowOpenInvite;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAllowedDomains() {
        return allowedDomains;
    }

    public void setAllowedDomains(String allowedDomains) {
        this.allowedDomains = allowedDomains;
    }

    public String getInviteId() {
        return inviteId;
    }

    public void setInviteId(String inviteId) {
        this.inviteId = inviteId;
    }

    public boolean isAllowOpenInvite() {
        return allowOpenInvite;
    }

    public void setAllowOpenInvite(boolean allowOpenInvite) {
        this.allowOpenInvite = allowOpenInvite;
    }

}
