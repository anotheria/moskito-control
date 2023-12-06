package org.moskito.control.plugins.sms;

import org.configureme.ConfigurationManager;
import org.moskito.control.plugins.notifications.AbstractStatusChangeNotifier;
import org.moskito.control.plugins.notifications.BaseNotificationPlugin;

/**
 * Plugin for sms notifications.
 * Sends messages via sms, specified in configuration file
 * on any component status change, if status change notifications is not muted.
 */
public class SmsPlugin extends BaseNotificationPlugin {

    @Override
    protected AbstractStatusChangeNotifier buildNotifier(String configurationName) {
        SmsConfig config = new SmsConfig();
        ConfigurationManager.INSTANCE.configureAs(config, configurationName);
        return new StatusChangeSmsNotifier(config);
    }

}
