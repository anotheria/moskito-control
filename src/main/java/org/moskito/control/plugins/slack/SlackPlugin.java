package org.moskito.control.plugins.slack;

import org.configureme.ConfigurationManager;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.plugins.AbstractMoskitoControlPlugin;

/**
 * Plugin for Slack notifications.
 * Sends messages to slack channel, specified in configuration file
 * on any component status change, if status change notifications is not muted.
 */
public class SlackPlugin extends AbstractMoskitoControlPlugin{

    /**
     * Configuration for Slack notifications
     */
    private SlackConfig config = new SlackConfig();

    /**
     * Status change listener for Slack plugin
     * Initialize on initialize() method call.
     * Link needs to be stored here for detaching it in deInitialize() method
     */
    private StatusChangeSlackNotifier notifier;

    @Override
    public void setConfigurationName(String configurationName) {
        ConfigurationManager.INSTANCE.configureAs(config, configurationName);
    }

    @Override
    public void initialize() {

        notifier = new StatusChangeSlackNotifier(config);

        // Attaching listener to event dispatcher for sending messages to slack on status change
        ApplicationRepository.getInstance()
                .getEventsDispatcher().addStatusChangeListener(notifier);

    }

    @Override
    public void deInitialize() {
        // Removing listener, messages to Slack will not been send from now
        ApplicationRepository.getInstance()
                .getEventsDispatcher().removeStatusChangeListener(notifier);
    }

}
