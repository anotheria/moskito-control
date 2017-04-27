package org.moskito.control.core.notification;

import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.mail.MailService;
import org.moskito.control.mail.MailServiceConfig;
import org.moskito.control.mail.message.MailMessageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Status changes mail notifier. Sends emails on any component status changes.
 *
 * @author vkazhdan
 */
public final class StatusChangeMailNotifier extends AbstractStatusChangeNotifier {

    /**
     * Constructor. Registers itself as the status change listener.
     */
    private StatusChangeMailNotifier() {
        ApplicationRepository.getInstance().getEventsDispatcher().addStatusChangeListener(this);
    }


    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(StatusChangeMailNotifier.class);

    public static StatusChangeMailNotifier getInstance() {
        return StatusChangeMailNotifierInstanceHolder.INSTANCE;
    }

    @Override
    public void notifyStatusChange(StatusChangeEvent event) {
        log.debug("Processing via mail notifier  status change event: " + event);

		if (!MoskitoControlConfiguration.getConfiguration().isMailNotificationEnabled()){
			log.debug("Mail notifications are disabled");
			return;
		}

        String[] notificationRecipients = MailServiceConfig.getInstance().getNotificationsMap().get(event.getStatus().getHealth());
		MailService.getInstance().send(MailMessageBuilder.buildStatusChangedMessage(event, notificationRecipients));
        log.warn("Notification mail was send for status change event: " + event);
    }

    /**
     * Singleton instance holder class.
     */
    private static class StatusChangeMailNotifierInstanceHolder {
        /**
         * Singleton instance.
         */
        private static final StatusChangeMailNotifier INSTANCE = new StatusChangeMailNotifier();
    }
}
