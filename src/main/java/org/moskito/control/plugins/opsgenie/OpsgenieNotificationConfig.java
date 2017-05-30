package org.moskito.control.plugins.opsgenie;

import org.apache.commons.lang.ArrayUtils;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.slack.NotificationStatusChange;

/**
 * OpsGenie configuration unit for per-status notification of specified recipients.
 */
@ConfigureMe
public class OpsgenieNotificationConfig {

    /**
     * Array of applications appliable to this configuration
     */
    @Configure
    private String[] applications = new String[0];

    /**
     * Statuses changes appliable for this config
     */
    @Configure
    private NotificationStatusChange[] notificationStatusChanges = new NotificationStatusChange[0];

    /**
     * Mail recipients.
     */
    @Configure
    private String[] recipients = new String[0];

    /**
     * Teams responsible for alerts
     */
    @Configure
    private String[] teams = new String[0];

    /**
     * Tags of alert
     */
    @Configure
    private String[] tags = new String[0];

    /**
     * OpsGenie account custom actions
     */
    @Configure
    private String[] actions = new String[0];

    public String[] getRecipients() {
        return recipients;
    }

    public void setRecipients(String[] recipients) {
        this.recipients = recipients;
    }

    public String[] getTeams() {
        return teams;
    }

    public void setTeams(String[] teams) {
        this.teams = teams;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String[] getActions() {
        return actions;
    }

    public void setActions(String[] actions) {
        this.actions = actions;
    }

    public void setApplications(String[] applications){
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

        if(!ArrayUtils.contains(applications, event.getApplication().getName()))
            return false; // Check is this config contains application

        if(notificationStatusChanges.length == 0)
            return true; // No status changes criteria is configured. Any will pass

        for (NotificationStatusChange statusChange : notificationStatusChanges)
            if(statusChange.isAppliableToEvent(event.getStatus().getHealth(), event.getOldStatus().getHealth()))
                return true; // Event status change match with status changes in config found

        return false; // Event don`t match any status change criteria

    }

    public void setNotificationStatusChanges(NotificationStatusChange[] notificationStatusChanges) {
        this.notificationStatusChanges = notificationStatusChanges;
    }

}