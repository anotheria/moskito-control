package org.moskito.control.plugins.mattermost.api;

import org.moskito.control.plugins.mattermost.api.annotations.FieldName;

/**
 * Class to make authorization requests using username and password.
 */
class AuthRequest extends BasePostRequest<AuthResponse> {

    /**
     * Mattermost user username
     */
    @FieldName(name = "login_id")
    private String username;
    /**
     * Mattermost user password
     */
    private String password;

    /**
     * Calls parent constructor with class of AuthResponse and api from arguments.
     * Initialize fields of username and password from arguments.
     * @param api Mattermost API wrapper main class object. Needed to be passed to parent
     * @param username Mattermost user username
     * @param password Mattermost user password
     */
    AuthRequest(MattermostApi api, String username, String password) {
        super(AuthResponse.class, api);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
