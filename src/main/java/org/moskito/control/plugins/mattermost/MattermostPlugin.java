package org.moskito.control.plugins.mattermost;

import org.configureme.ConfigurationManager;
import org.moskito.control.plugins.notifications.AbstractStatusChangeNotifier;
import org.moskito.control.plugins.notifications.BaseNotificationPlugin;

/**
 * Plugin for Mattermost notifications.
 * Sends messages to Mattermost channel, specified in configuration file
 * on any component status change, if status change notifications is not muted.
 */
public class MattermostPlugin extends BaseNotificationPlugin {

    @Override
    protected AbstractStatusChangeNotifier buildNotifier(String configurationName) {

        MattermostConfig config = new MattermostConfig();
        ConfigurationManager.INSTANCE.configureAs(config, configurationName);

        return new StatusChangeMattermostNotifier(config);

    }

}
