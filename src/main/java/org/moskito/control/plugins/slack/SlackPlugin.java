package org.moskito.control.plugins.slack;

import org.configureme.ConfigurationManager;
import org.moskito.control.plugins.notifications.AbstractStatusChangeNotifier;
import org.moskito.control.plugins.notifications.BaseNotificationPlugin;

/**
 * Plugin for Slack notifications.
 * Sends messages to slack channel, specified in configuration file
 * on any component status change, if status change notifications is not muted.
 */
public class SlackPlugin extends BaseNotificationPlugin{

    @Override
    protected AbstractStatusChangeNotifier buildNotifier(String configurationName) {
        SlackConfig config = new SlackConfig();
        ConfigurationManager.INSTANCE.configureAs(config, configurationName);
        return new StatusChangeSlackNotifier(config);
    }

}
