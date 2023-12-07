package org.moskito.control.plugins.sms;

import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.notifications.NotificationUtils;


/**
 * Builds request object to send notification sms
 * from configuration data and status change event
 */
public class SmsMessageBuilder {
    /**
     * Source status change event
     */
    private StatusChangeEvent event;
    /**
     * Template for alert links leads to component where status changes
     */
    private String alertLinkTemplate;

    /**
     * Builds request object filling it parameters.
     * @return request object ready to make request. (In case all builder parameters fill)
     */
    public String build(){

        //2023-12-07, Leon, reduced amount of characters in a message.

        String text = event.getComponent().getName();
        text += " status changed " + event.getOldStatus()+" -> "+event.getStatus();
        return text;
    }

    public SmsMessageBuilder setEvent(StatusChangeEvent event) {
        this.event = event;
        return this;
    }

    public SmsMessageBuilder setAlertLinkTemplate(String alertLinkTemplate) {
        this.alertLinkTemplate = alertLinkTemplate;
        return this;
    }

}
