package org.moskito.control.plugins.opsgenie;

import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.plugins.notifications.config.BaseNotificationProfileConfig;
import org.moskito.control.plugins.notifications.config.NotificationStatusChange;

/**
 * OpsGenie configuration unit for per-status notification of specified recipients.
 */
@ConfigureMe
public class OpsgenieNotificationConfig extends BaseNotificationProfileConfig{

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

    @Override
    public String[] getApplications() {
        return applications;
    }

    @Override
    public NotificationStatusChange[] getNotificationStatusChanges() {
        return notificationStatusChanges;
    }

    public void setNotificationStatusChanges(NotificationStatusChange[] notificationStatusChanges) {
        this.notificationStatusChanges = notificationStatusChanges;
    }

}