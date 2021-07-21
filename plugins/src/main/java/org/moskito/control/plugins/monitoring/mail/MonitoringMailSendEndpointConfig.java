package org.moskito.control.plugins.monitoring.mail;

import org.configureme.annotations.Configure;

public class MonitoringMailSendEndpointConfig {
    /**
     * External api endpoint to send.
     */
    @Configure
    private String apiEndpoint;
    /**
     * Email where to send.
     */
    @Configure
    private String email;
    /**
     * Name for basic auth.
     */
    @Configure
    private String basicAuthName;
    /**
     * Password for basic auth.
     */
    @Configure
    private String basicAuthPass;

    /**
     * Header name for authentication via header.
     */
    @Configure
    private String authHeaderName;
    /**
     * Header value for authentication via header.
     */
    @Configure
    private String authHeaderValue;

    public String getApiEndpoint() {
        return apiEndpoint;
    }

    public void setApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBasicAuthName() {
        return basicAuthName;
    }

    public void setBasicAuthName(String basicAuthName) {
        this.basicAuthName = basicAuthName;
    }

    public String getBasicAuthPass() {
        return basicAuthPass;
    }

    public void setBasicAuthPass(String basicAuthPass) {
        this.basicAuthPass = basicAuthPass;
    }

    public String getAuthHeaderName() {
        return authHeaderName;
    }

    public void setAuthHeaderName(String authHeaderName) {
        this.authHeaderName = authHeaderName;
    }

    public String getAuthHeaderValue() {
        return authHeaderValue;
    }

    public void setAuthHeaderValue(String authHeaderValue) {
        this.authHeaderValue = authHeaderValue;
    }

    @Override
    public String toString() {
        return "MonitoringMailSendEndpointConfig{" +
                "apiEndpoint='" + apiEndpoint + '\'' +
                ", basicAuthName='" + basicAuthName + '\'' +
                ", basicAuthPass='" + basicAuthPass + '\'' +
                ", authHeaderName='" + authHeaderName + '\'' +
                ", authHeaderValue='" + authHeaderValue + '\'' +
                '}';
    }
}
