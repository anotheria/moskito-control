package org.moskito.control.plugins.mail;

import org.moskito.control.core.notification.AbstractStatusChangeNotifier;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.mail.core.MailService;
import org.moskito.control.plugins.mail.core.message.MailMessageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Status changes core notifier. Sends emails on any component status changes.
 *
 * @author vkazhdan
 */
public final class StatusChangeMailNotifier extends AbstractStatusChangeNotifier {

    /**
     * Configuration for mail notifications
     */
    private MailServiceConfig config;

    /**
     *
     */
    private MailService mailService;

    /**
     * Sets configuration
     * @param config configuration for notifications
     */
    StatusChangeMailNotifier(MailServiceConfig config) {
        this.config = config;
        mailService = new MailService(config);
    }

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(StatusChangeMailNotifier.class);

    @Override
    public void notifyStatusChange(StatusChangeEvent event) {

        log.debug("Processing via core notifier  status change event: {}", event);

        Optional<MailNotificationConfig> notificationConfig = config.getProfileForEvent(event);

        if(!notificationConfig.isPresent()){
            log.info("No recipients found for event {}", event);
            return;
        }

        mailService.send(MailMessageBuilder.buildStatusChangedMessage(event, config, notificationConfig.get().getRecipients()));

        log.warn("Notification core was send for status change event: {}", event);

    }
    
}
