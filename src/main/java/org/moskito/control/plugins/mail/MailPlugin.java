package org.moskito.control.plugins.mail;

import org.configureme.ConfigurationManager;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.plugins.AbstractMoskitoControlPlugin;

/**
 * Plugin for Mail notifications.
 * Sends messages email address, specified in configuration file
 * on any component status change, if status change notifications is not muted.
 */
public class MailPlugin extends AbstractMoskitoControlPlugin {

    /**
     * Path to mail configuration
     */
    private String configurationName;

    /**
     * Status change listener for OpsGenie plugin
     * Initialize on initialize() method call.
     * Link needs to be stored here for detaching it in deInitialize() method
     */
    private StatusChangeMailNotifier notifier;

    @Override
    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }

    @Override
    public void initialize() {

        MailServiceConfig config = new MailServiceConfig();
        ConfigurationManager.INSTANCE.configureAs(config, configurationName);

        notifier = new StatusChangeMailNotifier(config);

        // Attaching listener to event dispatcher for sending alerts to email on status change
        ApplicationRepository.getInstance()
                .getEventsDispatcher().addStatusChangeListener(notifier);

    }

    @Override
    public void deInitialize() {
        // Removing listener, alerts to email will not been send from now
        ApplicationRepository.getInstance()
                .getEventsDispatcher().removeStatusChangeListener(notifier);
    }

}
