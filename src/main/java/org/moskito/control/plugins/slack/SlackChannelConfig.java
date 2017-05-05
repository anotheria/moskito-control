package org.moskito.control.plugins.slack;

import org.apache.commons.lang.ArrayUtils;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.core.status.StatusChangeEvent;

/**
 * Configuration for single Slack channel
 * Links channel with applications
 */
@ConfigureMe
public class SlackChannelConfig {

    /**
     * Name of Slack channel
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
    private String[] notificationStatuses;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getApplications() {
        return applications;
    }

    public void setApplications(String[] applications) {
        this.applications = applications;
    }

    /**
     * Check is this channel configured to catch up this event.
     * Check is carried out by event application and status
     * @param event event to check
     * @return true - message should be send to this channel
     *         false - sending message, composed by this event, to this channel if not configured
     */
    public boolean isAppliableToEvent(StatusChangeEvent event){
               // Check is this config contains application
        return ArrayUtils.contains(applications, event.getApplication().getName()) &&
                (       // If this config not contain statuses, then all statuses pass
                        notificationStatuses == null || notificationStatuses.length == 0 ||
                        // Check is event status registered in this config
                        ArrayUtils.contains(notificationStatuses, event.getStatus().getHealth().name())
                );
    }

    public void setNotificationStatuses(String[] notificationStatuses) {
        this.notificationStatuses = notificationStatuses;
    }
}
