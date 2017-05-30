package org.moskito.control.plugins.mattermost.api;

import org.apache.http.HttpResponse;

import java.io.IOException;

/**
 * Response class for auth request.
 * Overwrites {@link BaseResponse#populateResponse(HttpResponse)} method
 * because response of Mattermost API contains token in headers. (Base class parses response from json body)
 */
class AuthResponse extends BaseResponse {

    private String token;

    @Override
    public void populateResponse(HttpResponse response) throws IOException {
        token = response.getFirstHeader("Token").getValue();
    }

    String getToken() {
        return token;
    }

}
