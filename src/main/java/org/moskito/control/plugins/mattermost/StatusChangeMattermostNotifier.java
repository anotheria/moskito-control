package org.moskito.control.plugins.mattermost;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.mattermost.api.MattermostApi;
import org.moskito.control.plugins.mattermost.api.exceptions.MattermostAPIException;
import org.moskito.control.plugins.notifications.AbstractStatusChangeNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Status change Mattermost notifier.
 * Sends messages to specified in Mattermost configuration chat on any component status change
 */
public class StatusChangeMattermostNotifier extends AbstractStatusChangeNotifier<MattermostChannelConfig> {

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
     * Constructor, that retrieves config object and
     * initialize Mattermost API wrapper
     * @param config Mattermost notification plugin config
     */
    public StatusChangeMattermostNotifier(MattermostConfig config) {

        super(config);
        this.config = config;

        try {

            api = new MattermostApi(
                    config.getHost(),
                    config.getUsername(),
                    config.getPassword()
            );

        } catch (ReflectiveOperationException | JsonProcessingException e) {
            log.error("Mattermost API wrapper module error occurred", e);
        } catch (IOException e) {
            log.warn("IO exception while trying to make Mattermost auth request", e);
        } catch (MattermostAPIException e) {
            log.warn("Mattermost api returned error while trying to authenticate", e);
        }

    }

    @Override
    public void notifyStatusChange(StatusChangeEvent event, MattermostChannelConfig profile) {
        try {

            api.createPost(
                    new MattermostMessageBuilder()
                        .setAlertLinkTemplate(config.getAlertLink())
                        .setThumbImageBasePath(config.getBaseImageUrlPath())
                        .setApi(api)
                        .setTeamName(config.getTeamName())
                        .setChannel(profile.getName())
                        .setEvent(event)
                        .build()
            );

        } catch (JsonProcessingException | ReflectiveOperationException e) {
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

    @Override
    public Logger getLogger() {
        return log;
    }

}
