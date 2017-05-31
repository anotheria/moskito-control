package org.moskito.control.plugins.notifications.config;

import org.apache.commons.lang.ArrayUtils;
import org.moskito.control.core.status.StatusChangeEvent;


public abstract class BaseNotificationProfileConfig {

    public abstract String[] getApplications();
    public abstract NotificationStatusChange[] getNotificationStatusChanges();


    /**
     * Check is this channel configured to catch up this event.
     * Check is carried out by event application and status
     * @param event event to check
     * @return true - message should be send to this channel
     *         false - sending message, composed by this event, to this channel if not configured
     */
    public boolean isAppliableToEvent(StatusChangeEvent event){

        // Check is this config contains application
        if (!ArrayUtils.contains(getApplications(), event.getApplication().getName()))
            return false;
        if (getNotificationStatusChanges().length==0)
            return true;
        for (NotificationStatusChange change : getNotificationStatusChanges()){
            if (change.isAppliableToEvent(event.getStatus().getHealth(), event.getOldStatus().getHealth())){
                return true;
            }
        }
        return false;

    }

}
