package org.moskito.control.plugins.mattermost;

import de.arkraft.mattermost.MattermostV3;
import org.moskito.control.core.notification.AbstractStatusChangeNotifier;
import org.moskito.control.core.status.StatusChangeEvent;

public class StatusChangeMattermostNotifier extends AbstractStatusChangeNotifier {

    private MattermostConfig config;

    /**
     * Builds message string.
     * @param event status change event, source of data for message
     * @return status change event message
     */
    private String buildMessage(StatusChangeEvent event){

        String componentNameMessagePart = event.getApplication().getName() + ":" + event.getComponent();

        return  componentNameMessagePart + " status changed to " + event.getStatus();

    }

    public StatusChangeMattermostNotifier(MattermostConfig config) {
        this.config = config;
    }

    @Override
    public void notifyStatusChange(StatusChangeEvent event) {

        MattermostV3 mattermost = new MattermostV3("localhost:8065", "alert-bot", "lol");

    }

}
