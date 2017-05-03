package org.moskito.control.plugins.opsgenie;

import org.configureme.annotations.Configure;
import org.moskito.control.core.HealthColor;

/**
 * OpsGenie configuration unit for per-status notification of specified recipients.
 */
public class OpsgenieNotificationConfig {

    /**
     * Status value.
     */
    @Configure
    private HealthColor guardedStatus;

    /**
     * Mail recipients.
     */
    @Configure
    private String[] recipients = new String[0];

    /**
     * Teams responsible for alerts
     */
    @Configure
    private String[] teams = new String[0];

    /**
     * Tags of alert
     */
    @Configure
    private String[] tags = new String[0];

    /**
     * OpsGenie account custom actions
     */
    @Configure
    private String[] actions = new String[0];


    HealthColor getGuardedStatus() {
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

    public String[] getTeams() {
        return teams;
    }

    public void setTeams(String[] teams) {
        this.teams = teams;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String[] getActions() {
        return actions;
    }

    public void setActions(String[] actions) {
        this.actions = actions;
    }

}