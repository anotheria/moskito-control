package org.moskito.control.plugins.logfile;

import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.notifications.config.BaseNotificationPluginConfig;

import java.util.Arrays;

/**
 * Configuration for Status change log file plugin
 */
@ConfigureMe
public class StatusChangeLogFilePluginConfig extends BaseNotificationPluginConfig<LogFileConfig> {

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

    @Override
    protected LogFileConfig[] getProfileConfigs() {
        return files;
    }

}
