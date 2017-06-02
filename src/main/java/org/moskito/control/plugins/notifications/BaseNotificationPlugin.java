package org.moskito.control.plugins.notifications;

import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.plugins.AbstractMoskitoControlPlugin;

/**
 * Basic class for notification plugins
 * Contains definitions of initialize() and deInitialize()
 * methods to attach and detach status listener,
 * witch retrieves by buildNotifier(String) method
 * defined in child classes
 */
public abstract class BaseNotificationPlugin extends AbstractMoskitoControlPlugin {

    /**
     * Path to configuration of plugin
     */
    private String configurationName;

    /**
     * Status change listener for notification plugins
     * Initialize on initialize() method call.
     * Link needs to be stored here for detaching it in deInitialize() method
     */
    private AbstractStatusChangeNotifier notifier;

    /**
     * Method to create and configure notifier instance of plugin
     *
     * @param configurationName name of ConfigureMe configuration for this plugin
     * @return AbstractStatusChangeNotifier implementation instance
     */
    protected abstract AbstractStatusChangeNotifier buildNotifier(String configurationName);

    @Override
    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }

    /**
     * Attaches notifier to listen events
     */
    @Override
    public void initialize() {

        notifier = buildNotifier(configurationName);

        ApplicationRepository.getInstance()
                .getEventsDispatcher().addStatusChangeListener(notifier);

    }

    /**
     * Detaches notifier
     */
    @Override
    public void deInitialize() {
        ApplicationRepository.getInstance()
                .getEventsDispatcher().removeStatusChangeListener(notifier);
    }


}
