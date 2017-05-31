package org.moskito.control.plugins.mail;

import org.configureme.ConfigurationManager;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.notification.AbstractStatusChangeNotifier;
import org.moskito.control.plugins.AbstractMoskitoControlPlugin;
import org.moskito.control.plugins.notifications.BaseNotificationPlugin;

/**
 * Plugin for Mail notifications.
 * Sends messages email address, specified in configuration file
 * on any component status change, if status change notifications is not muted.
 */
public class MailPlugin extends BaseNotificationPlugin {

    @Override
    protected AbstractStatusChangeNotifier buildNotifier(String configurationName) {
        MailServiceConfig config = new MailServiceConfig();
        ConfigurationManager.INSTANCE.configureAs(config, configurationName);
        return new StatusChangeMailNotifier(config);
    }

}
