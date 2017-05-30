package org.moskito.control.plugins.mattermost;

import net.anotheria.util.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.notification.AbstractStatusChangeNotifier;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.mattermost.api.MattermostApi;
import org.moskito.control.plugins.mattermost.api.exceptions.MattermostAPIException;
import org.moskito.control.plugins.mattermost.api.exceptions.MattermostAPIInternalException;
import org.moskito.control.plugins.mattermost.api.posts.CreatePostRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Status change Mattermost notifier.
 * Sends messages to specified in Mattermost configuration chat on any component status change
 */
public class StatusChangeMattermostNotifier extends AbstractStatusChangeNotifier {

    private static final String LINK_KEYWORD_APPLICATION = "${APPLICATION}";

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(StatusChangeMattermostNotifier.class);

    /**
     * Mattermost plugin configuration
     */
    private MattermostConfig config;
    /**
     * Mattermost API helper
     */
    private MattermostApi api;

    /**
     * Builds link to application, that will be placed to notification message.
     * Takes link template from configuration file
     * @param event current event
     * @return url leads to application, noted in status change event
     */
    private String buildAlertLink(StatusChangeEvent event){
        String link = config.getAlertLink();
        link = StringUtils.replace(link, LINK_KEYWORD_APPLICATION, event.getApplication().getName() );
        return link;
    }

    /**
     * Retrieves link to image for inserting it in notification message
     * Takes base url from configuration and inserts color name to end of template string.
     * For green status full url to image would look like:
     *      "http://www.moskito.org/applications/control/green.png"
     * @param color color of new status
     * @return url to status image
     */
    private String getThumbImageUrlByColor(HealthColor color){
        return config.getBaseImageUrlPath() + color.name().toLowerCase() + ".png";
    }

    /**
     * Builds insertion with status image corresponding to mattermost text
     * formatting guide.
     * See https://docs.mattermost.com/help/messaging/formatting-text.html for more.
     * @param event current event
     * @return chunk of text, that will be rendered as image in message
     */
    private String buildImageLink(StatusChangeEvent event){

        String healthName = event.getStatus().getHealth().name();

        return "![" + healthName + "]" +
                "(" + getThumbImageUrlByColor(event.getStatus().getHealth())  +
                " \"Status changed to " + healthName + "\")";

    }

    /**
     * Builds header of notification message.
     * Header contain application and component names and its new status
     * Header may also contain link to moskito-control application (depends on mattermost plugin config).
     * @param event current event
     * @return chunk of formatted text that will be rendered as header
     */
    private String buildHeader(StatusChangeEvent event){

        String componentNameMessagePart = event.getApplication().getName() + ":" + event.getComponent();

        if(config.getAlertLink() != null) // inserting link to component name if it set in config
            componentNameMessagePart = "[" + componentNameMessagePart + "]" + "(" + buildAlertLink(event) + ")";

        return  "### " + componentNameMessagePart + " status changed to " +
                event.getStatus().getHealth().name() + buildImageLink(event) + "\n";

    }

    /**
     * Builds message string using mattermost text formatting
     * @param event status change event, source of data for message
     * @return status change event message
     */
    private String buildMessage(StatusChangeEvent event){

        // Removing square bracers from event message
        String eventMessage = event.getStatus().getMessages().toString();
        eventMessage = eventMessage.substring(1, eventMessage.length() - 1);

        return buildHeader(event) + eventMessage + "\n" +
                "#### NewStatus\n" + event.getStatus().getHealth().name() + "\n" +
                "#### OldStatus\n" + event.getOldStatus().getHealth().name() + "\n" +
                "#### " + "Timestamp\n" +
                NumberUtils.makeISO8601TimestampString(event.getTimestamp());

    }

    /**
     * Constructor, that retrieves config object and
     * initialize Mattermost API wrapper
     * @param config Mattermost notification plugin config
     */
    public StatusChangeMattermostNotifier(MattermostConfig config) {

        this.config = config;

        try {

            api = new MattermostApi(
                    config.getHost(),
                    config.getUsername(),
                    config.getPassword()
            );

        } catch (MattermostAPIInternalException e) {
            log.error("Mattermost API wrapper module error occurred", e);
        } catch (IOException e) {
            log.warn("IO exception while trying to make Mattermost auth request", e);
        } catch (MattermostAPIException e) {
            log.warn("Mattermost api returned error while trying to authenticate", e);
        }

    }

    @Override
    public void notifyStatusChange(StatusChangeEvent event) {

        log.debug("Processing via slack notifier status change event: " + event);

        String channelForApplication = config.getChannelNameForEvent(event);

        if(channelForApplication == null){
            log.info("Channel not set for application " + event.getApplication().getName()
                    + " sending Slack canceled.");
            return;
        }

        try {
            api.createPost(
                    new CreatePostRequestBuilder(api)
                    .setTeamName(config.getTeamName())
                    .setChannelName(channelForApplication)
                    .setMessage(buildMessage(event))
                    .build()
            );
        } catch (MattermostAPIInternalException e) {
            log.error("Mattermost API wrapper error occurred " +
                    "while trying to send notification message", e);
        } catch (IOException e) {
            log.error("IO exception occurred" +
                    "while trying to send notification message", e);
        } catch (MattermostAPIException e) {
            log.error("Mattermost API returned error " +
                    "while trying to send notification message", e);
        }

    }

}
