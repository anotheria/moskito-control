package org.moskito.control.plugins.mattermost;

import net.anotheria.util.NumberUtils;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.mattermost.api.MattermostApi;
import org.moskito.control.plugins.mattermost.api.exceptions.MattermostAPIException;
import org.moskito.control.plugins.mattermost.api.posts.CreatePostRequest;
import org.moskito.control.plugins.mattermost.api.posts.CreatePostRequestBuilder;
import org.moskito.control.plugins.notifications.NotificationUtils;

import java.io.IOException;

/**
 * Builds create post request
 * to send notification message to Mattermost
 * using mattermost configuration data
 * and status change event
 */
public class MattermostMessageBuilder {

    /**
     * Team name to post message
     */
    private String teamName;
    /**
     * Channel name to post notification
     */
    private String channel;
    /**
     * Source status change event
     */
    private StatusChangeEvent event;
    /**
     * Template for alert links leads to component where status changes
     */
    private String alertLinkTemplate;
    /**
     * Base to path to images that inserted in message
     */
    private String thumbImageBasePath;
    /**
     * Mattermost API instance
     * token takes from here
     */
    private MattermostApi api;

    /**
     * Create create post request object
     * from filled data
     * @return create post request object ready to make request
     * @throws ReflectiveOperationException thrown by Mattermost API wrapper
     * @throws IOException thrown by Mattermost API wrapper
     * @throws MattermostAPIException thrown by Mattermost API wrapper
     */
    public CreatePostRequest build() throws ReflectiveOperationException, IOException, MattermostAPIException {

        CreatePostRequestBuilder postRequestBuilder = new CreatePostRequestBuilder(api)
                .setTeamName(teamName)
                .setChannelName(channel);

        StringBuilder message = new StringBuilder();
        HealthColor health = event.getStatus().getHealth();

        // STARTING BUILD HEADER

            // Creating application and component name string as header. ### makes next line header
            String componentNameMessagePart = "### " + event.getApplication().getName() + ":" + event.getComponent();

            // Inserting link to component name if it set in config and appending it to message header
            if(alertLinkTemplate != null)
                message.append("[").append(componentNameMessagePart).append("]")
                        .append("(").append(NotificationUtils.buildAlertLink(alertLinkTemplate, event)).append(")");
            else
                message.append(componentNameMessagePart); // Appending message header without link

            // Appending status change info to message header
            message.append(" status changed to ")
                    .append(event.getStatus().getHealth().name());

            // Appending image link to header
            message.append("![").append(health.name()).append("]") // Alt text
                    .append("(")
                    .append(NotificationUtils.getThumbImageUrlByColor(thumbImageBasePath, health)) // Image link
                    .append(" \"Status changed to ").append(health.name()).append("\")\n"); // Hover text

        // END BUILDING HEADER

        // START BUILDING MESSAGE BODY

                message.append("#### NewStatus\n") // #### makes this line sub header
                        .append(event.getStatus().getHealth().name()).append("\n")
                        .append("#### OldStatus\n")
                        .append(event.getOldStatus().getHealth().name()).append("\n")
                        .append("#### ").append("Timestamp\n")
                        .append(NumberUtils.makeISO8601TimestampString(event.getTimestamp()));

        // END BUILDING MESSAGE BODY

        // Appending message to request
        postRequestBuilder.setMessage(message.toString());

        return postRequestBuilder.build();

    }

    public MattermostMessageBuilder setTeamName(String teamName) {
        this.teamName = teamName;
        return this;
    }

    public MattermostMessageBuilder setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public MattermostMessageBuilder setEvent(StatusChangeEvent event) {
        this.event = event;
        return this;
    }

    public MattermostMessageBuilder setAlertLinkTemplate(String alertLinkTemplate) {
        this.alertLinkTemplate = alertLinkTemplate;
        return this;
    }

    public MattermostMessageBuilder setThumbImageBasePath(String thumbImageBasePath) {
        this.thumbImageBasePath = thumbImageBasePath;
        return this;
    }

    public MattermostMessageBuilder setApi(MattermostApi api) {
        this.api = api;
        return this;
    }
}
