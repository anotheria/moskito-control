package org.moskito.control.plugins.mattermost;

import org.configureme.ConfigurationManager;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.plugins.AbstractMoskitoControlPlugin;

/**
 * Plugin for Mattermost notifications.
 * Sends messages to Mattermost channel, specified in configuration file
 * on any component status change, if status change notifications is not muted.
 */
public class MattermostPlugin extends AbstractMoskitoControlPlugin {

    /**
     * Path to configuration of Mattermost plugin
     */
    private String configurationName;

    /**
     * Status change listener for Mattermost plugin
     * Initialize on initialize() method call.
     * Link needs to be stored here for detaching it in deInitialize() method
     */
    private StatusChangeMattermostNotifier notifier;

    @Override
    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }

    @Override
    public void initialize() {

        MattermostConfig config = new MattermostConfig();
        ConfigurationManager.INSTANCE.configureAs(config, configurationName);

        notifier = new StatusChangeMattermostNotifier(config);

        // Attaching listener to event dispatcher for sending messages to Mattermost on status change
        ApplicationRepository.getInstance()
                .getEventsDispatcher().addStatusChangeListener(notifier);

    }

    @Override
    public void deInitialize() {
        // Removing listener, messages to Mattermost will not been send from now
        ApplicationRepository.getInstance()
                .getEventsDispatcher().removeStatusChangeListener(notifier);
    }

}
