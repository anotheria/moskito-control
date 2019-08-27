package org.moskito.control.plugins.logfile;

import org.configureme.ConfigurationManager;
import org.moskito.control.core.ComponentRepository;
import org.moskito.control.plugins.AbstractMoskitoControlPlugin;

/**
 * Main class of status change log file plugin.
 * Registers notifier, that writes status change messages into files
 * specified in plugin configuration.
 */
public class StatusLogFilePlugin extends AbstractMoskitoControlPlugin{

    /**
     * Path to configuration of status log file plugin
     */
    private String configurationName;

    /**
     * Status change listener for status change log plugin
     * Initialize on initialize() method call.
     * Link needs to be stored here for detaching it in deInitialize() method
     */
    private StatusChangeLogFileNotifier notifier;

    @Override
    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }

    @Override
    public void initialize() {

        StatusChangeLogFilePluginConfig config = new StatusChangeLogFilePluginConfig();
        ConfigurationManager.INSTANCE.configureAs(config, configurationName);

        notifier = new StatusChangeLogFileNotifier(config);

        // Attaching listener to event dispatcher
        ComponentRepository.getInstance()
                .getEventsDispatcher().addStatusChangeListener(notifier);

    }

    @Override
    public void deInitialize() {
        // Removing listener, logs will not been written from now
        ComponentRepository.getInstance()
                .getEventsDispatcher().removeStatusChangeListener(notifier);
    }

}
