package org.moskito.control.plugins.mattermost;

import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.plugins.notifications.config.BaseNotificationProfileConfig;
import org.moskito.control.plugins.notifications.config.NotificationStatusChange;

/**
 * Configuration for single Mattermost channel
 * Links channel with applications
 */
@ConfigureMe
public class MattermostChannelConfig extends BaseNotificationProfileConfig{

    /**
     * Name of Mattermost channel
     */
    @Configure
    private String name;

    /**
     * Applications names linked to this channel
     */
    @Configure
    private String[] applications;

    /**
     * List of component statuses to send notifications.
     * If empty - notifications will send on all statuses
     */
    @Configure
    private NotificationStatusChange[] notificationStatusChanges = new NotificationStatusChange[0];

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getApplications() {
        return applications;
    }

    public NotificationStatusChange[] getStatusChanges() {
        return notificationStatusChanges;
    }

    public void setApplications(String[] applications) {
        this.applications = applications;
    }

    public void setNotificationStatusChanges(NotificationStatusChange[] notificationStatusChanges) {
        this.notificationStatusChanges = notificationStatusChanges;
    }
    
}
