package org.moskito.control.plugins.sms;

import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.notifications.AbstractStatusChangeNotifier;
import org.moskito.control.plugins.sms.provider.SmsProvider;
import org.moskito.control.plugins.sms.provider.SmsProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatusChangeSmsNotifier extends AbstractStatusChangeNotifier<SmsNotificationConfig> {

    /**
     * Configuration for sms notifications
     */
    private SmsConfig config;

    /**
     *
     */
    private SmsProvider provider;

    private final static String WHATSAPP_TWILIO_PREFIX = "whatsapp:";

    /**
     * Sets configuration
     * @param config configuration for notifications
     */
    StatusChangeSmsNotifier(SmsConfig config) {
        super(config);
        this.config = config;
        provider = SmsProviderFactory.getProvider(SmsProviderType.TWILIO, config);
    }

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(StatusChangeSmsNotifier.class);

    @Override
    public void notifyStatusChange(StatusChangeEvent event, SmsNotificationConfig profile) {

        String content = new SmsMessageBuilder()
                .setEvent(event)
                .setAlertLinkTemplate(config.getAlertLink())
                .build();

        for (String recipient: profile.getRecipients()) {
            provider.send(recipient, content);
        }

        for (String recipient: profile.getWaRecipients()) {
            provider.send(WHATSAPP_TWILIO_PREFIX + recipient, content);
        }

        log.info("Notification core was send for status change event: {}", event);
    }

    @Override
    public Logger getLogger() {
        return log;
    }

}
