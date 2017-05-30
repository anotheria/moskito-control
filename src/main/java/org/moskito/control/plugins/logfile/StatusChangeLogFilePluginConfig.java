package org.moskito.control.plugins.logfile;

import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.core.status.StatusChangeEvent;

import java.util.Arrays;

/**
 * Configuration for Status change log file plugin
 */
@ConfigureMe
public class StatusChangeLogFilePluginConfig {

    /**
     * Configuration for each log file
     */
    @Configure
    private LogFileConfig[] files;

    public LogFileConfig[] getFiles() {
        return files;
    }

    public void setFiles(LogFileConfig[] files) {
        this.files = files;
    }

    /**
     * Finds files configs suites for event, specified in arguments.
     * @param event event to search log file configurations
     * @return configuration of log files for event
     */
    public LogFileConfig[] getFilesForEvent(StatusChangeEvent event){

        Object[] fileConfigs = Arrays.stream(files)
                .filter(file -> file.isAppliableToEvent(event))
                .toArray();

        return Arrays.copyOf(fileConfigs, fileConfigs.length, LogFileConfig[].class);

    }

}
