package org.moskito.control.plugins.mail;

import org.apache.commons.lang.ArrayUtils;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.slack.NotificationStatusChange;

/**
 * Mail configuration unit for per-status notification of specified recipients.
 *
 * @author Vladyslav Bezuhlyi
 */
@ConfigureMe
public class MailNotificationConfig {

    /**
     * Applications names linked to this channel
     */
    @Configure
    private String[] applications = new String[0];

    /**
     * List of component statuses to send notifications.
     * If empty - notifications will send on all statuses
     */
    @Configure
    private NotificationStatusChange[] notificationStatusChanges = new NotificationStatusChange[0];

    /**
     * Mail recipients.
     */
    @Configure
    private String[] recipients;

    /**
     * Check is this notifications config is configured to catch up this event.
     * Check is carried out by event application and status
     * @param event event to check
     * @return true - message should be send to recipients of this config
     *         false - no
     */
    public boolean isAppliableToEvent(StatusChangeEvent event){
        // Check is this config contains application
        if(!ArrayUtils.contains(applications, event.getApplication().getName()))
            return false;
        if(notificationStatusChanges.length == 0)
            return true; // No status change configured. All statuses pass

        for (NotificationStatusChange statusChange : notificationStatusChanges)
            if(statusChange.isAppliableToEvent(event.getStatus().getHealth(), event.getOldStatus().getHealth()))
                return true; // Status change found in statuses change array

        return false; // event don`t match any status change criteria

    }

    public String[] getRecipients() {
        return recipients;
    }

    public void setRecipients(String[] recipients) {
        this.recipients = recipients;
    }

    public void setApplications(String[] applications) {
        this.applications = applications;
    }

    public void setNotificationStatusChanges(NotificationStatusChange[] notificationStatusChanges) {
        this.notificationStatusChanges = notificationStatusChanges;
    }
}
