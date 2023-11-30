package org.moskito.control.plugins.sms;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.notifications.config.BaseNotificationProfileConfig;
import org.moskito.control.plugins.notifications.config.NotificationStatusChange;

@ConfigureMe
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2", "EI_EXPOSE_REP"},
        justification = "This is the way configureMe works, it provides beans for access")
public class SmsNotificationConfig extends BaseNotificationProfileConfig {

    /**
     * List of component statuses to send notifications.
     * If empty - notifications will send on all statuses
     */
    @Configure
    private NotificationStatusChange[] notificationStatusChanges = new NotificationStatusChange[0];

    /**
     *  Regular sms recipients phone numbers.
     */
    @Configure
    private String[] recipients;

    /**
     *  Whatsapp recipients phone numbers.
     */
    @Configure
    private String[] waRecipients;


    public NotificationStatusChange[] getStatusChanges() {
        return notificationStatusChanges;
    }

    /**
     * Check is this notifications config is configured to catch up this event.
     * Check is carried out by event application and status
     * @param event event to check
     * @return true - message should be send to recipients of this config
     *         false - no
     */
    public boolean isAppliableToEvent(StatusChangeEvent event){
        // Check is this config contains application
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

    public String[] getWaRecipients() {
        return waRecipients;
    }

    public void setWaRecipients(String[] waRecipients) {
        this.waRecipients = waRecipients;
    }

    public void setNotificationStatusChanges(NotificationStatusChange[] notificationStatusChanges) {
        this.notificationStatusChanges = notificationStatusChanges;
    }

}
