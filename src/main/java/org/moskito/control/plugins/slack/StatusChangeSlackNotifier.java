package org.moskito.control.plugins.slack;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.request.chat.ChatPostMessageRequest;
import com.github.seratch.jslack.api.methods.response.chat.ChatPostMessageResponse;
import com.github.seratch.jslack.api.model.Attachment;
import com.github.seratch.jslack.api.model.Field;
import net.anotheria.util.NumberUtils;
import net.anotheria.util.StringUtils;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.notification.AbstractStatusChangeNotifier;
import org.moskito.control.core.status.StatusChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Status change Slack notifier.
 * Sends messages to specified in slack configuration chat on any component status change
 */
public class StatusChangeSlackNotifier extends AbstractStatusChangeNotifier {

	public static final String LINK_KEYWORD_APPLICATION = "${APPLICATION}";

    /**
     * Name of not in channel Slack API error
     *
     */
    private final static String NOT_IN_CHANNEL_ERROR_NAME = "not_in_channel";

    /**
     * Object to invoke requests to Slack API
     */
    private Slack slack = new Slack();

    /**
     * Configuration for slack notifications
     */
    private SlackConfig config;

    /**
     * Is bot invited to specified in config channel
     *
     * If true - request be send with "asUser" parameter.
     * Requests with "asUser" parameter set to true
     * relates message to bot.
     * Otherwise message author be without avatar and with default bot username
     *
     * Setting "asUser" to true
     * will cause an error, if bot not joined to channel
     *
     * By default - true. However, actual value
     * be defined after first request. If first response
     * returned with not joined to channel error,
     * next requests will not be "as user",
     * failed request be resend.
     *
     */
    private Map<String, Boolean> inChannel = new HashMap<>();

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(StatusChangeSlackNotifier.class);

    public StatusChangeSlackNotifier(SlackConfig config) {
        this.config = config;
        // Initialize channels map by default to true
		for(String channelName : config.getRegisteredChannels())
			inChannel.put(channelName, true);
		// Putting default channel to map if it was configured
		if(config.getDefaultChannel() != null)
			inChannel.put(config.getDefaultChannel(), true);
    }

	private String getThumbImageUrlByColor(HealthColor color){
		return config.getBaseImageUrlPath() + color.name().toLowerCase() + ".png";
	}

    /**
     * Builds message string.
     * @param event status change event, source of data for message
     * @return status change event message
     */
    private String buildMessage(StatusChangeEvent event){

    	String componentNameMessagePart = event.getApplication().getName() + ":" + event.getComponent();

    	if(config.getAlertLink() != null) // inserting link to component name if it set in config
    		componentNameMessagePart = "<" + buildAlertLink(event) + "|" + componentNameMessagePart + ">";

        return  componentNameMessagePart + " status changed to " + event.getStatus();

    }

	/** test scope **/ static String color2color(HealthColor color){
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

	/** test scope **/ String buildAlertLink(StatusChangeEvent event){
    	String link = config.getAlertLink();
    	link = StringUtils.replace(link, LINK_KEYWORD_APPLICATION, event.getApplication().getName() );
    	return link;
	}

	private Field buildField(String title, String value){
		return Field.builder().title(title).value(value).valueShortEnough(true).build();

	}

    private Attachment buildAttachment(StatusChangeEvent event){
    	Attachment.AttachmentBuilder builder = Attachment.builder();

    	String text = event.getApplication().getName()+":"+event.getComponent().getName()+" status changed from "+event.getOldStatus()+" to "+event.getStatus()+" @ "+ NumberUtils.makeISO8601TimestampString(event.getTimestamp());
    	builder.color(color2color(event.getStatus().getHealth()));
    	builder.fallback(text);
    	//builder.text(text); -- we don't need text, we cover all with fields.

		LinkedList<Field> fields = new LinkedList<>();
		fields.add(buildField("NewStatus", event.getStatus().getHealth().toString()));
    	fields.add(buildField("OldStatus", event.getOldStatus().getHealth().toString()));
		fields.add(buildField("Timestamp", NumberUtils.makeISO8601TimestampString(event.getTimestamp())));
		builder.fields(fields);

		builder.thumbUrl(
				getThumbImageUrlByColor(event.getStatus().getHealth())
		);
    	
    	return builder.build();

	}



    @Override
    public void notifyStatusChange(StatusChangeEvent event) {

        log.debug("Processing via slack notifier status change event: " + event);

        try {

			LinkedList<Attachment> attachments = new LinkedList<>(); attachments.add(buildAttachment(event));
            ChatPostMessageRequest.ChatPostMessageRequestBuilder requestBuilder = ChatPostMessageRequest.builder()
                    .token(config.getBotToken())
                    .text(buildMessage(event))
					.attachments(attachments);

            String channelForApplication = config.getChannelNameForEvent(event);

            if(channelForApplication == null){
            	log.info("Channel not set for application " + event.getApplication().getName()
						+ " sending Slack canceled.");
            	return;
			}

			requestBuilder.channel(channelForApplication);

            if(inChannel.get(channelForApplication))
                requestBuilder.asUser(true);

            ChatPostMessageResponse postResponse =
                    slack.methods().chatPostMessage(requestBuilder.build());

            if(postResponse.isOk())
                log.debug(
                        "Slack notification was send for status change event: " + event +
                                "with response \n" + postResponse.toString()
                );

            else{

                if(postResponse.getError().equals(NOT_IN_CHANNEL_ERROR_NAME)){
                    // If bot not in channel, next requests be done with "as user" parameter set to false
                    // Bot avatar and username be not shown
					inChannel.put(channelForApplication, false);
                    log.error("Bot is not joined to channel." +
                            " Making request again with \"asUser\" parameter set to false");
                    notifyStatusChange(event);

                }
                else
                    log.error("Failed to send Slack notification with API error " + postResponse.getError());

            }

        } catch (IOException | SlackApiException e) {
            log.error("Failed to send Slack notification", e);
        }

    }

}