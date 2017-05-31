package org.moskito.control.plugins.notifications;

import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.notification.AbstractStatusChangeNotifier;
import org.moskito.control.plugins.AbstractMoskitoControlPlugin;

public abstract class BaseNotificationPlugin extends AbstractMoskitoControlPlugin {

    /**
     * Path to configuration of Slack plugin
     */
    private String configurationName;

    /**
     * Status change listener for Slack plugin
     * Initialize on initialize() method call.
     * Link needs to be stored here for detaching it in deInitialize() method
     */
    private AbstractStatusChangeNotifier notifier;

    protected abstract AbstractStatusChangeNotifier buildNotifier(String configurationName);

    @Override
    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }

    @Override
    public void initialize() {

        notifier = buildNotifier(configurationName);

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
