package org.moskito.control.plugins.opsgenie;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.plugins.notifications.config.BaseNotificationPluginConfig;

/**
 * Config for OpsGenie API
 * Config file defines by plugin config
 */
@ConfigureMe
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2", "EI_EXPOSE_REP"},
        justification = "This is the way configureMe works, it provides beans for access")
public class OpsgenieConfig extends BaseNotificationPluginConfig<OpsgenieNotificationConfig>{

    /**
     * Sender of alert
     */
    @Configure
    private String alertSender;

    /**
     * Entry of alert
     */
    @Configure
    private String alertEntity;

    /**
     * Api key of opsGenie account
     */
    @Configure
    private String apiKey;

    /**
     * Notifications.
     */
    @Configure
    private OpsgenieNotificationConfig[] notifications;

    public void setNotifications(OpsgenieNotificationConfig[] notifications) {
        this.notifications = notifications;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getAlertEntity() {
        return alertEntity;
    }

    public void setAlertEntity(String alertEntity) {
        this.alertEntity = alertEntity;
    }

    public String getAlertSender() {
        return alertSender;
    }

    public void setAlertSender(String alertSender) {
        this.alertSender = alertSender;
    }

    @Override
    protected OpsgenieNotificationConfig[] getProfileConfigs() {
        return notifications;
    }

}