package org.moskito.control.plugins.sms.budgetsms;

import org.configureme.ConfigurationManager;
import org.moskito.control.plugins.notifications.AbstractStatusChangeNotifier;
import org.moskito.control.plugins.notifications.BaseNotificationPlugin;

/**
 * Plugin for sms notifications.
 * Sends messages via sms, specified in configuration file
 * on any component status change, if status change notifications is not muted.
 */

public class BudgetSmsMessagingPlugin extends BaseNotificationPlugin {

    @Override
    protected AbstractStatusChangeNotifier buildNotifier(String configurationName) {
        BudgetSmsMessagingConfig config = new BudgetSmsMessagingConfig();
        ConfigurationManager.INSTANCE.configureAs(config, configurationName);
        return new AlternateStatusChangeSmsNotifier(config);
    }

}
