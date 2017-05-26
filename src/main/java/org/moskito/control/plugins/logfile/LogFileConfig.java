package org.moskito.control.plugins.logfile;

import org.apache.commons.lang.ArrayUtils;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.core.status.StatusChangeEvent;

/**
 * Configuration for single log file of
 * status change log file plugin.
 * Specifies path to log file and requirements for event
 * to write log in file of this config.
 */
@ConfigureMe
public class LogFileConfig {

    /**
     * Array of applications to write notification in this file.
     * If null or empty - every application will pass
     */
    @Configure
    private String[] applications;
    /**
     * Path to file to write logs
     */
    @Configure
    private String path;
    /**
     * Previous status condition for this log file.
     * If from or to field is not specified - any status change pass
     */
    @Configure
    private String from;
    /**
     * Current status condition for this log file
     * If from or to field is not specified - any status change pass
     */
    @Configure
    private String to;

    public String[] getApplications() {
        return applications;
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Check, is event in arguments suites for this log file by
     * specified in this config conditions
     * @param event status change event
     * @return true  - event pass this log file requirements
     *         false - no
     */
    public boolean isAppliableToEvent(StatusChangeEvent event){

        return  // Check is application of event contains in this log applications list
                // or list is empty and every should application pass
                (applications == null || applications.length == 0 ||
                        ArrayUtils.contains(applications, event.getApplication().getName())) &&
                // Check is old and new status of event corresponds to log config
                // or from and to statuses not specified in this config and any of status changes pass
                (to == null || from == null ||
                        (from.equals(event.getOldStatus().getHealth().name())) &&
                        (to.equals(event.getStatus().getHealth().name()))
                );

    }

}
