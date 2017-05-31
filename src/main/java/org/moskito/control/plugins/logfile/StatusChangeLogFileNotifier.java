package org.moskito.control.plugins.logfile;

import net.anotheria.util.NumberUtils;
import org.moskito.control.core.notification.AbstractStatusChangeNotifier;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.logfile.utils.StatusLogFilesHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class StatusChangeLogFileNotifier extends AbstractStatusChangeNotifier {

    /**
     * Configuration for notifier
     */
    private StatusChangeLogFilePluginConfig config;
    /**
     * Holds log files object to write notifications
     */
    private StatusLogFilesHolder logFileHolder = new StatusLogFilesHolder();

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

       return "\nStatus changed.\n"
               + "Timestamp: " + NumberUtils.makeISO8601TimestampString((event.getTimestamp())) + "\n"
               + "Application: " + event.getApplication() + "\n"
               + "Component: " + event.getComponent() + "\n"
               + "Old status: " + event.getOldStatus() + "\n"
               + "New status: " + event.getStatus() + "\n";

    }

    @Override
    public void notifyStatusChange(StatusChangeEvent event) {

        log.debug("Processing via status log file notifier status change event: {}", event);

        Optional<LogFileConfig> fileConfigs = config.getProfileForEvent(event);

        if(!fileConfigs.isPresent()){
            return;
        }


//        try {
//
//            logFileHolder.getFileByPath(fileConfig.getPath())
//                    .writeToFile(buildMessage(event));
//
//        } catch (IOException | SecurityException e) {
//            log.warn("Failed to write status change log file", e);
//        }

    }

}
