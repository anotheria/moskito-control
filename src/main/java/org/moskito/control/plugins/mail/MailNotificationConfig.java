package org.moskito.control.plugins.mail;

import org.apache.commons.lang.ArrayUtils;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.status.StatusChangeEvent;

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
    private String[] applications;

    /**
     * List of component statuses to send notifications.
     * If empty - notifications will send on all statuses
     */
    @Configure
    private String[] notificationStatuses;

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
        return ArrayUtils.contains(applications, event.getApplication().getName()) &&
                (       // If this config not contain statuses, then all statuses pass
                        notificationStatuses == null || notificationStatuses.length == 0 ||
                                // Check is event status registered in this config
                                ArrayUtils.contains(notificationStatuses, event.getStatus().getHealth().name())
                );
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

    public void setNotificationStatuses(String[] notificationStatuses) {
        this.notificationStatuses = notificationStatuses;
    }

}
