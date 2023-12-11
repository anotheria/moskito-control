package org.moskito.control.plugins.sms.twilio;

import org.configureme.ConfigurationManager;
import org.moskito.control.plugins.notifications.AbstractStatusChangeNotifier;
import org.moskito.control.plugins.notifications.BaseNotificationPlugin;

/**
 * Plugin for sms notifications.
 * Sends messages via sms, specified in configuration file
 * on any component status change, if status change notifications is not muted.
 */
public class TwilioMessagingPlugin extends BaseNotificationPlugin {

    @Override
    protected AbstractStatusChangeNotifier buildNotifier(String configurationName) {
        TwilioMessagingConfig config = new TwilioMessagingConfig();
        ConfigurationManager.INSTANCE.configureAs(config, configurationName);
        return new StatusChangeSmsNotifier(config);
    }

}
