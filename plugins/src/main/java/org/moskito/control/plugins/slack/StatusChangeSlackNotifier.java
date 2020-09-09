package org.moskito.control.plugins.slack;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.request.chat.ChatPostMessageRequest;
import com.github.seratch.jslack.api.methods.response.chat.ChatPostMessageResponse;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.notifications.AbstractStatusChangeNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Status change Slack notifier.
 * Sends messages to specified in slack configuration chat on any component status change
 */
public class StatusChangeSlackNotifier extends AbstractStatusChangeNotifier<SlackChannelConfig> {

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
        super(config);
        this.config = config;
        // Initialize channels map by default to true
		for(String channelName : config.getRegisteredChannels())
			inChannel.put(channelName, true);
    }

	@Override
	public void notifyStatusChange(StatusChangeEvent event, SlackChannelConfig profile) {

    	String channelName = profile.getName();

		try {

			ChatPostMessageRequest request = new SlackMessageBuilder()
					.setChannel(channelName)
					.setAlertLinkTemplate(config.getAlertLink())
					.setThumbImageBasePath(config.getBaseImageUrlPath())
					.setToken(config.getBotToken())
					.setEvent(event)
					.setAsUser(inChannel.get(channelName))
					.build();

			ChatPostMessageResponse postResponse =
					slack.methods().chatPostMessage(request);

			if(postResponse.isOk()) {
				log.debug("Slack notification was send for status change event: {} with response \n {}", event, postResponse);

			}else{
				if(postResponse.getError().equals(NOT_IN_CHANNEL_ERROR_NAME)){
					// If bot not in channel, next requests be done with "as user" parameter set to false
					// Bot avatar and username be not shown
					inChannel.put(channelName, false);
					log.info("Bot is not joined to channel." +
							" Making request again with \"asUser\" parameter set to false");
					notifyStatusChange(event);

				}else {
					log.error("Failed to send Slack notification with API error {}", postResponse.getError());
				}

			}

		} catch (IOException | SlackApiException e) {
			log.error("Failed to send Slack notification", e);
		}

	}

	@Override
	public Logger getLogger() {
		return log;
	}

}
