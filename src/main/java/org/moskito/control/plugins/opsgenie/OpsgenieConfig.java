package org.moskito.control.plugins.opsgenie;

import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.core.status.StatusChangeEvent;

import java.util.Arrays;
import java.util.Optional;

/**
 * Config for OpsGenie API
 * Config file defines by plugin config
 */
@ConfigureMe
public class OpsgenieConfig {

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

    /**
     * Returns channel name for specified event or default channel (if it was configured)
     * @param event event to return it corresponding channel
     * @return slack channel name
     */
    public Optional<OpsgenieNotificationConfig> getNotificationConfigForEvent(StatusChangeEvent event){

        return Arrays.stream(notifications)
                .filter(channel -> channel.isAppliableToEvent(event))
                .findFirst();

    }

}