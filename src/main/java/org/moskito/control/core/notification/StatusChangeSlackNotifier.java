package org.moskito.control.core.notification;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.request.chat.ChatPostMessageRequest;
import com.github.seratch.jslack.api.methods.response.chat.ChatPostMessageResponse;
import net.anotheria.util.NumberUtils;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.config.notifiers.slack.SlackConfig;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.status.StatusChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Status change Slack notifier.
 * Sends messages to specified in slack configuration chat on any component status change
 */
public class StatusChangeSlackNotifier extends AbstractStatusChangeNotifier{

    /**
     * Name of not in channel Slack API error
     */
    private final static String NOT_IN_CHANNEL_ERROR_NAME = "not_in_channel";

    /**
     * Object to invoke requests to Slack API
     */
    private Slack slack = new Slack();

    /**
     * Configuration for slack notifications
     */
    private SlackConfig config = SlackConfig.getInstance();

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
    private boolean inChannel = true;


    /**
     * Constructor. Registers itself as the status change listener.
     */
    private StatusChangeSlackNotifier() {
        ApplicationRepository.getInstance().addStatusChangeListener(this);
    }

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(StatusChangeSlackNotifier.class);

    /**
     * Builds message string.
     * @param event status change event, source of data for message
     * @return status change event message
     */
    private String buildMessage(StatusChangeEvent event){
        return  "MOSKITO STATUS CHANGED\n"
                + "Timestamp: " + NumberUtils.makeISO8601TimestampString((event.getTimestamp())) + "\n"
                + "Application: " + event.getApplication() + "\n"
                + "Component: " + event.getComponent() + "\n"
                + "Old status: " + event.getOldStatus() + "\n"
                + "New status: " + event.getStatus() + "\n";
    }



    @Override
    public void notifyStatusChange(StatusChangeEvent event) {

        log.debug("Processing via slack notifier status change event: " + event);

        if (!MoskitoControlConfiguration.getConfiguration().isSlackNotificationEnabled()){
            log.debug("Slack notifications are disabled");
            return;
        }

        if (muter.isMuted()) {
            log.debug("Slack notifications are muted. Skipped notification Slack sending for status change event "
                    + event + ". Remaining muting time: " + getRemainingMutingTime());
            return;
        }

        try {

            ChatPostMessageRequest.ChatPostMessageRequestBuilder requestBuilder = ChatPostMessageRequest.builder()
                    .channel(config.getChannel())
                    .token(config.getBotToken())
                    .text(buildMessage(event));

            if(inChannel)
                requestBuilder.asUser(true);

            ChatPostMessageResponse postResponse =
                    slack.methods().chatPostMessage(requestBuilder.build());

            if(postResponse.isOk())
                log.warn(
                        "Slack notification was send for status change event: " + event +
                                "with response \n" + postResponse.toString()
                );

            else{

                if(postResponse.getError().equals(NOT_IN_CHANNEL_ERROR_NAME)){
                    // If bot not in channel, next requests be done with "as user" parameter set to false
                    // Bot avatar and username be not shown
                    inChannel = false;
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

    public static StatusChangeSlackNotifier getInstance() {
        return StatusChangeSlackNotifier.StatusChangeSlackNotifierInstanceHolder.INSTANCE;
    }

    /**
     * Singleton instance holder class.
     */
    private static class StatusChangeSlackNotifierInstanceHolder {
        /**
         * Singleton instance.
         */
        private static final StatusChangeSlackNotifier INSTANCE = new StatusChangeSlackNotifier();
    }

}
