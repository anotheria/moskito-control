package org.moskito.control.plugins.opsgenie;

import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.AfterReConfiguration;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.core.HealthColor;

import java.util.HashMap;
import java.util.Map;

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
    private String defaultAlertSender;

    /**
     * Entry of alert
     */
    @Configure
    private String defaultAlertEntity;

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

    /**
     * Array of mail notification recipients per application status.
     */
    private Map<HealthColor, OpsgenieNotificationConfig> notificationsMap;

    /**
     * Builds map with configurations
     * for different statuses
     */
    @AfterConfiguration
    @AfterReConfiguration
    public void updateNotificationsMap() {
        notificationsMap = new HashMap<>();
        for (OpsgenieNotificationConfig notification : notifications) {
            notificationsMap.put(notification.getGuardedStatus(), notification);
        }
    }

    public void setNotifications(OpsgenieNotificationConfig[] notifications) {
        this.notifications = notifications;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getDefaultAlertEntity() {
        return defaultAlertEntity;
    }

    public void setDefaultAlertEntity(String defaultAlertEntity) {
        this.defaultAlertEntity = defaultAlertEntity;
    }

    public String getDefaultAlertSender() {
        return defaultAlertSender;
    }

    public void setDefaultAlertSender(String defaultAlertSender) {
        this.defaultAlertSender = defaultAlertSender;
    }

    public OpsgenieNotificationConfig getConfigForHealth(HealthColor color){
        return notificationsMap.get(color);
    }

}