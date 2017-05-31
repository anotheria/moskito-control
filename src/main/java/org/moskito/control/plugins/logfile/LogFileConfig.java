package org.moskito.control.plugins.logfile;

import org.apache.commons.lang.ArrayUtils;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.notifications.config.BaseNotificationProfileConfig;
import org.moskito.control.plugins.notifications.config.NotificationStatusChange;

/**
 * Configuration for single log file of
 * status change log file plugin.
 * Specifies path to log file and requirements for event
 * to write log in file of this config.
 */
@ConfigureMe
public class LogFileConfig extends BaseNotificationProfileConfig{

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

    @Override
    public NotificationStatusChange[] getNotificationStatusChanges() {
        return notificationStatusChanges;
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

    public void setNotificationStatusChanges(NotificationStatusChange[] notificationStatusChanges) {
        this.notificationStatusChanges = notificationStatusChanges;
    }

}
