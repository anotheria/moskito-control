package org.moskito.control.plugins.logfile;

import org.apache.commons.lang.ArrayUtils;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.slack.NotificationStatusChange;

/**
 * Configuration for single log file of
 * status change log file plugin.
 * Specifies path to log file and requirements for event
 * to write log in file of this config.
 */
@ConfigureMe
public class LogFileConfig {

    /**
     * Array of applications to write notification in this file.
     * If null or empty - every application will pass
     */
    @Configure
    private String[] applications = new String[0];
    /**
     * Path to file to write logs
     */
    @Configure
    private String path;

    /**
     * List of component statuses to send notifications.
     * If empty - notifications will send on all statuses
     */
    @Configure
    private NotificationStatusChange[] notificationStatusChanges = new NotificationStatusChange[0];

    public String[] getApplications() {
        return applications;
    }

    public void setApplications(String[] applications) {
        this.applications = applications;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Check, is event in arguments suites for this log file by
     * specified in this config conditions
     * @param event status change event
     * @return true  - event pass this log file requirements
     *         false - no
     */
    public boolean isAppliableToEvent(StatusChangeEvent event){

        if(!ArrayUtils.contains(applications, event.getApplication().getName()))
            return false; // Event application is not appliable for this log file

        if(notificationStatusChanges.length == 0)
            return true; // No status changes configured. All status change pass

        for (NotificationStatusChange statusChange : notificationStatusChanges)
            if (statusChange.isAppliableToEvent(event.getStatus().getHealth(), event.getOldStatus().getHealth()))
                return true; // status change of event satisfy this config

        return false; // Event is not appliable by it`s old and new status

    }

    public void setNotificationStatusChanges(NotificationStatusChange[] notificationStatusChanges) {
        this.notificationStatusChanges = notificationStatusChanges;
    }

}
