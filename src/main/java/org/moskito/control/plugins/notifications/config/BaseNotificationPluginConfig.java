package org.moskito.control.plugins.notifications.config;

import org.moskito.control.core.status.StatusChangeEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class BaseNotificationPluginConfig <T extends BaseNotificationProfileConfig> {

    protected abstract T[] getProfileConfigs();

    /**
     * Returns channel name for specified event or default channel (if it was configured)
     * @param event event to return it corresponding channel
     * @return slack channel name
     */
    public Optional<T> getProfileForEvent(StatusChangeEvent event){

        return Arrays.stream(getProfileConfigs())
                .filter(profile -> profile.isAppliableToEvent(event))
                .findFirst();

    }

}
