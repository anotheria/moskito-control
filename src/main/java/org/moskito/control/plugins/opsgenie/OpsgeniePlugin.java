package org.moskito.control.plugins.opsgenie;

import org.configureme.ConfigurationManager;
import org.moskito.control.plugins.notifications.AbstractStatusChangeNotifier;
import org.moskito.control.plugins.notifications.BaseNotificationPlugin;

/**
 * Plugin for OpsGenie notifications.
 * Sends alert to OpsGenie account, specified in configuration file
 * on any component status change, if status change notifications is not muted.
 */
public class OpsgeniePlugin extends BaseNotificationPlugin {

    @Override
    protected AbstractStatusChangeNotifier buildNotifier(String configurationName) {

        OpsgenieConfig config = new OpsgenieConfig();
        ConfigurationManager.INSTANCE.configureAs(config, configurationName);

        return new StatusChangeOpsgenieNotifier(config);

    }

}