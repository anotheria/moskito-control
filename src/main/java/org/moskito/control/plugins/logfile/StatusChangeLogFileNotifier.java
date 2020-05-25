package org.moskito.control.plugins.logfile;

import net.anotheria.util.NumberUtils;
import org.moskito.control.plugins.notifications.AbstractStatusChangeNotifier;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.logfile.utils.LogFileAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatusChangeLogFileNotifier extends AbstractStatusChangeNotifier<LogFileConfig> {

    /**
     * Logger
     */
    private static Logger log = LoggerFactory.getLogger(StatusChangeLogFileNotifier.class);

    /**
     * Initialize config by constructor argument
     * @param config configuration of notifier plugin
     */
    public StatusChangeLogFileNotifier(StatusChangeLogFilePluginConfig config) {
        super(config);
    }

    /**
     * Builds message that be written to log file
     * @param event current status change event. Source of message
     * @return status change message string
     */
    private String buildMessage(StatusChangeEvent event){
    	return new StringBuilder(NumberUtils.makeISO8601TimestampString((event.getTimestamp()))).append(' ').
				append(event.getComponent()).append(' ').
				append(event.getOldStatus()).append(" -> ").append(event.getStatus()).toString();
    }

    @Override
    public void notifyStatusChange(StatusChangeEvent event, LogFileConfig profile) {
        try {

            LogFileAppender.writeToFile(
                    profile.getPath(),
                    buildMessage(event)
            );

        } catch (Exception e) {
            log.warn("Failed to write status change log file", e);
        }
    }

    @Override
    public Logger getLogger() {
        return log;
    }

}
