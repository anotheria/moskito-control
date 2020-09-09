package org.moskito.control.plugins.notifications.config;

import org.moskito.control.core.status.StatusChangeEvent;

/**
 * Basic class for notification plugins profiles configurations
 * (Example : different channels in slack plugin)
 * Contain method to filter profiles by applications and colors of old and new statuses of event
 */
public abstract class BaseNotificationProfileConfig {


    /**
     * Method must return array of NotificationStatusChange objects
     * that contains criteria for old and new status of event.
     * If event satisfy one of this criteria it can be processed
     * by this profile
     * @return array of from-to statuses configurations
     */
    public abstract NotificationStatusChange[] getStatusChanges();

    /**
     * Check is this profile configured to catch up this event.
     * Check is carried out by event application and status
     * @param event event to check
     * @return true - message should be send to this channel
     *         false - sending message, composed by this event, to this channel if not configured
     */
    public boolean isAppliableToEvent(StatusChangeEvent event){

        if (getStatusChanges().length==0)
            return true;
        for (NotificationStatusChange change : getStatusChanges()){
            if (change.isAppliableToEvent(event.getStatus().getHealth(), event.getOldStatus().getHealth())){
                return true;
            }
        }
        return false;

    }

}
