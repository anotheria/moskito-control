package org.moskito.control.plugins.monitoring.mail;

import com.google.gson.annotations.SerializedName;
import org.configureme.annotations.Configure;

public class MonitoringMailConfig {

    @Configure
    private String name;

    @Configure
    @SerializedName("@fetchMailConfig")
    private MonitoringMailFetchConfig fetchMailConfig;

    /**
     * External api endpoint to send
     */
    @Configure
    @SerializedName("@sendMailConfig")
    private MonitoringMailSendEndpointConfig sendMailConfig;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MonitoringMailFetchConfig getFetchMailConfig() {
        return fetchMailConfig;
    }

    public void setFetchMailConfig(MonitoringMailFetchConfig fetchMailConfig) {
        this.fetchMailConfig = fetchMailConfig;
    }

    public MonitoringMailSendEndpointConfig getSendMailConfig() {
        return sendMailConfig;
    }

    public void setSendMailConfig(MonitoringMailSendEndpointConfig sendMailConfig) {
        this.sendMailConfig = sendMailConfig;
    }

    @Override
    public String toString() {
        return "MonitoringMailConfig{" +
                "fetchConfig=" + fetchMailConfig +
                ", sendMailEndpoint=" + sendMailConfig +
                '}';
    }
}
