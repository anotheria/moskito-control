package org.moskito.control.plugins.mail;

import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.mail.core.MailService;
import org.moskito.control.plugins.mail.core.message.MailMessageBuilder;
import org.moskito.control.plugins.notifications.AbstractStatusChangeNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Status changes core notifier. Sends emails on any component status changes.
 *
 * @author vkazhdan
 */
public final class StatusChangeMailNotifier extends AbstractStatusChangeNotifier<MailNotificationConfig> {

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
        super(config);
        this.config = config;
        mailService = new MailService(config);
    }

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(StatusChangeMailNotifier.class);

    @Override
    public void notifyStatusChange(StatusChangeEvent event, MailNotificationConfig profile) {

        mailService.send(
                MailMessageBuilder.buildStatusChangedMessage(
                        event, config, profile.getRecipients()
                )
        );

        log.info("Notification core was send for status change event: {}", event);

    }

    @Override
    public Logger getLogger() {
        return log;
    }

}
