package org.moskito.control.plugins.logfile;

import net.anotheria.util.NumberUtils;
import org.moskito.control.core.notification.AbstractStatusChangeNotifier;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.logfile.utils.LogFileAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StatusChangeLogFileNotifier extends AbstractStatusChangeNotifier {

    /**
     * Configuration for notifier
     */
    private StatusChangeLogFilePluginConfig config;

    /**
     * Logger
     */
    private static Logger log = LoggerFactory.getLogger(StatusChangeLogFileNotifier.class);

    /**
     * Initialize config by constructor argument
     * @param config configuration of notifier plugin
     */
    public StatusChangeLogFileNotifier(StatusChangeLogFilePluginConfig config) {
        this.config = config;
    }

    /**
     * Builds message that be written to log file
     * @param event current status change event. Source of message
     * @return status change message string
     */
    private String buildMessage(StatusChangeEvent event){
        return "Timestamp " + NumberUtils.makeISO8601TimestampString((event.getTimestamp()))
            + " Application " + event.getApplication()
            + " Component " + event.getComponent()
            + " OldStatus " + event.getOldStatus()
            + " NewStatus " + event.getStatus();
    }

    @Override
    public void notifyStatusChange(StatusChangeEvent event) {

        log.debug("Processing via status log file notifier status change event: {}", event);

        List<LogFileConfig> fileConfigs = config.getProfileForEvent(event);

        if(fileConfigs.isEmpty()){
            log.info("Failed to find log files for event : {}", event);
            return;
        }


        for (LogFileConfig config : fileConfigs)
            try {

                LogFileAppender.writeToFile(
                        config.getPath(),
                        buildMessage(event)
                );

            } catch (Exception e) {
                log.warn("Failed to write status change log file", e);
            }

    }

}
