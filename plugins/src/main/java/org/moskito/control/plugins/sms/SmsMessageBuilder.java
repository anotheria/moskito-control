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

        String content = event.getComponent().getName();
        if(alertLinkTemplate != null) // inserting link to component name if it set in config
            content =
                    "<" + NotificationUtils.buildAlertLink(alertLinkTemplate, event) + "|" + content + ">";
        content += " status changed to " + event.getStatus();

        return content;
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
