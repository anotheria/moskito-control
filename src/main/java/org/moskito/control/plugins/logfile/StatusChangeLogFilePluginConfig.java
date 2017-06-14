package org.moskito.control.plugins.logfile;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.plugins.notifications.config.BaseNotificationPluginConfig;

/**
 * Configuration for Status change log file plugin
 */
@ConfigureMe
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2", "EI_EXPOSE_REP"},
        justification = "This is the way configureMe works, it provides beans for access")
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
