package org.moskito.control.mail;

import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.core.HealthColor;

/**
 * Configuration unit for per-status notification of specified recipients.
 *
 * @author Vladyslav Bezuhlyi
 */
@ConfigureMe
public class NotificationConfig {

    /**
     * Status value.
     */
    @Configure
    private HealthColor guardedStatus;

    /**
     * Mail recipients.
     */
    @Configure
    private String[] recipients;


    public HealthColor getGuardedStatus() {
        return guardedStatus;
    }

    public void setGuardedStatus(HealthColor guardedStatus) {
        this.guardedStatus = guardedStatus;
    }

    public String[] getRecipients() {
        return recipients;
    }

    public void setRecipients(String[] recipients) {
        this.recipients = recipients;
    }
}
