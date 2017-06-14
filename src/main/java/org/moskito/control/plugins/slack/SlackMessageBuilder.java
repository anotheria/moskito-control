package org.moskito.control.plugins.slack;

import com.github.seratch.jslack.api.methods.request.chat.ChatPostMessageRequest;
import com.github.seratch.jslack.api.model.Attachment;
import com.github.seratch.jslack.api.model.Field;
import net.anotheria.util.NumberUtils;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.notifications.NotificationUtils;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Builds request object to post notification message to Slack
 * from configuration data and status change event
 */
public class SlackMessageBuilder {

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
     * Is required to make asUser request to post message
     * Avatar and name are shown in that case, but posting to not joined in channels is disabled
     */
    private boolean asUser;
    /**
     * Bot auth token
     */
    private String token;

    /**
     * Builds attachment filed for slack message
     * @param title name of filed
     * @param value value of filed
     * @return filed object
     */
    private Field buildField(String title, String value){
        return Field.builder().title(title).value(value).valueShortEnough(true).build();
    }

    /**
     * Transforms HealthColor object to
     * color hex code
     * @param color color to take hex code
     * @return hex code of color suite to color from arguments
     */
    /* test scope */ static String color2color(HealthColor color){
        switch(color){
            case GREEN:
                return "#94cc19";
            case RED:
                return "#fc3e39";
            case ORANGE:
                return "#ff8400";
            case YELLOW:
                return "#f4e300";
            case PURPLE:
                return "#ff53d6";
            case NONE:
                return "#cccccc";
            default:
                throw new IllegalArgumentException("Color "+color+" is not yet mapped");

        }

    }

    /**
     * Builds request object filling it parameters.
     * @return request object ready to make request. (In case all builder parameters fill)
     */
    public ChatPostMessageRequest build(){

        ChatPostMessageRequest.ChatPostMessageRequestBuilder requestBuilder = ChatPostMessageRequest.builder()
                .token(token)
                .asUser(asUser)
                .channel(channel);

        // START COMPOSING MESSAGE HEADER

            // Putting application and component name to message header
            String messageHeader = event.getApplication().getName() + ":" + event.getComponent();

            if(alertLinkTemplate != null) // inserting link to component name if it set in config
                messageHeader =
                        "<" + NotificationUtils.buildAlertLink(alertLinkTemplate, event) + "|" + messageHeader + ">";

            messageHeader += " status changed to " + event.getStatus(); // Adding status change to message header
            requestBuilder.text(messageHeader); // attaching message header to request

        // END COMPOSING MESSAGE TEXT

        // START COMPOSING ATTACHMENTS

            Attachment.AttachmentBuilder attachmentBuilder = Attachment.builder();

            String fallbackText = event.getApplication().getName() + ":"
                        + event.getComponent().getName()
                        +" status changed from " + event.getOldStatus()
                        + " to " + event.getStatus() + " @ "
                        + NumberUtils.makeISO8601TimestampString(event.getTimestamp());

            attachmentBuilder.color(color2color(event.getStatus().getHealth()));
            attachmentBuilder.fallback(fallbackText);

            // Building fields in message
            LinkedList<Field> fields = new LinkedList<>();
            fields.add(buildField("NewStatus", event.getStatus().getHealth().toString()));
            fields.add(buildField("OldStatus", event.getOldStatus().getHealth().toString()));
            fields.add(buildField("Timestamp", NumberUtils.makeISO8601TimestampString(event.getTimestamp())));
            attachmentBuilder.fields(fields);

            attachmentBuilder.thumbUrl(
                    NotificationUtils.getThumbImageUrlByColor(thumbImageBasePath, event.getStatus().getHealth())
            );

            requestBuilder.attachments(
                    Collections.singletonList(attachmentBuilder.build())
            );

        // END COMPOSING ATTACHMENTS

        return requestBuilder.build();

    }

    public SlackMessageBuilder setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public SlackMessageBuilder setEvent(StatusChangeEvent event) {
        this.event = event;
        return this;
    }

    public SlackMessageBuilder setAlertLinkTemplate(String alertLinkTemplate) {
        this.alertLinkTemplate = alertLinkTemplate;
        return this;
    }

    public SlackMessageBuilder setThumbImageBasePath(String thumbImageBasePath) {
        this.thumbImageBasePath = thumbImageBasePath;
        return this;
    }

    public SlackMessageBuilder setAsUser(boolean asUser) {
        this.asUser = asUser;
        return this;
    }

    public SlackMessageBuilder setToken(String token) {
        this.token = token;
        return this;
    }

}