package org.moskito.control.plugins.notifications;

import org.moskito.control.core.status.MuteEventListener;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.core.status.StatusChangeListener;
import org.moskito.control.plugins.notifications.config.BaseNotificationPluginConfig;
import org.moskito.control.plugins.notifications.config.BaseNotificationProfileConfig;
import org.slf4j.Logger;

import java.util.List;

/**
 * Basic class for notifications.
 * Contains definition of notifyStatusChange(StatusChangeEvent)
 * that searches profiles configs for event
 * and invokes overloaded notifyStatusChange method with event and profile arguments
 * for each profile, that satisfy event criteria
 */
public abstract class AbstractStatusChangeNotifier <T extends BaseNotificationProfileConfig> implements StatusChangeListener, MuteEventListener {

    /**
     * General configuration of notifier.
     * Profiles in notifyStatusChange(StatusChangeEvent event) are taken from here.
     * Initializes by constructor parameter.
     */
    private BaseNotificationPluginConfig<T> config;

    /**
     * @param config plugin configuration, that contains profiles
     */
    protected AbstractStatusChangeNotifier(BaseNotificationPluginConfig<T> config) {
        this.config = config;
    }

    /**
     * Called whenever a status of a component is changed.
     * Find profiles for event and calls notifyStatusChange(StatusChangeEvent, T(profile))
     * method for each profile. This method must be defined in child class.
     * Actual notification goes there.
     * @param event status change event
     */
    public void notifyStatusChange(StatusChangeEvent event){

        getLogger().debug("Starting to process status change event");

        List<T> profiles = config.getProfileForEvent(event);

        if(profiles.isEmpty()){
            getLogger().info("No profiles found for event : {}. Skipping notification send", event);
        }

        for (T profile : profiles)
            notifyStatusChange(event, profile);

    }

    /**
     * Process notification event with profile
     * @param event status change event
     * @param profile profile suite to this event
     */
    public abstract void notifyStatusChange(StatusChangeEvent event, T profile);

    /**
     * Returns logger bound to specific child class.
     * Required to determinate from where logs is go
     * @return logger instance bound to child class
     */
    public abstract Logger getLogger();

}
