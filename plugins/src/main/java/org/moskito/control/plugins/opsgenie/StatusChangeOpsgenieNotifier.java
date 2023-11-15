package org.moskito.control.plugins.opsgenie;

import com.ifountain.opsgenie.client.OpsGenieClient;
import com.ifountain.opsgenie.client.OpsGenieClientException;
import com.ifountain.opsgenie.client.model.alert.CreateAlertRequest;
import com.ifountain.opsgenie.client.model.alert.CreateAlertResponse;
import net.anotheria.util.NumberUtils;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.notifications.AbstractStatusChangeNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

/**
 * Status change OpsGenie notifier.
 * Sends alerts to OpsGenie on any component status change
 */
public final class StatusChangeOpsgenieNotifier extends AbstractStatusChangeNotifier<OpsgenieNotificationConfig> {

    /**
     * Configuration of OpsGenie notifications
     */
    private OpsgenieConfig config;
    /**
     * Provides the client for accessing the OpsGenie web service.
     */
    private OpsGenieClient client = new OpsGenieClient();

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(StatusChangeOpsgenieNotifier.class);

    public StatusChangeOpsgenieNotifier(OpsgenieConfig config) {
        super(config);
        this.config = config;
    }

    /**
     * Builds alert description string.
     * @param event status change event, source of data for description
     * @return status change event description
     */
    private String buildDescription(StatusChangeEvent event){
        return  "Timestamp: " + NumberUtils.makeISO8601TimestampString((event.getTimestamp())) + "\n"
                + "Component: " + event.getComponent() + "\n"
                + "Old status: " + event.getOldStatus() + "\n"
                + "New status: " + event.getStatus() + "\n";
    }

    /**
     * Builds alert message string.
     * By API specification length of message is limited to 130 symbols,
     * if length of message be bigger - string will be cut.
     * @param event status change event, source of data for message
     * @return status change event message
     */
    private String buildMessage(StatusChangeEvent event){
        String messageString = "Status Changed from " + event.getOldStatus().getHealth() +
               " to " + event.getStatus().getHealth() 
                + " in component " + event.getComponent();

        // Max allowed size of message is 130 characters
        if(messageString.length() > 130)
            messageString = messageString.substring(0, 129) + '…';

        return messageString;

    }

    /**
     * Builds instance of com.ifountain.opsgenie.client.model.alert.CreateAlertRequest class,
     * that belongs to OpsGenie java SDK using data from event and opsgenie configuration.
     *
     * @param event status change event, source of data to form request
     * @return instance of CreateAlertRequest object ready to make request
     */
    private CreateAlertRequest createAlertRequest(StatusChangeEvent event, OpsgenieNotificationConfig notificationConfig){

        CreateAlertRequest request = new CreateAlertRequest();

        request.setApiKey(config.getApiKey());
        request.setMessage(buildMessage(event));
        request.setDescription(buildDescription(event));
        request.setSource(config.getAlertSender());
        request.setEntity(config.getAlertEntity());

        // Skip filling status-specified data, if it`s not present
        if(notificationConfig != null) {
            request.setActions(
                    Arrays.asList(notificationConfig.getActions())
            );
            request.setTags(
                    Arrays.asList(notificationConfig.getTags())
            );
            request.setRecipients(
                    Arrays.asList(notificationConfig.getRecipients())
            );
            request.setTeams(
                    Arrays.asList(notificationConfig.getTeams())
            );
        }

        return request;

    }

    @Override
    public void notifyStatusChange(StatusChangeEvent event, OpsgenieNotificationConfig profile) {

        try {

            CreateAlertResponse response = client.alert().createAlert(
                    createAlertRequest(event, profile)
            );

            String alertId = response.getAlertId();

            log.debug(
                    "OpsGenie notification was send for status change event: {} with alertId {}", event, alertId
            );

        } catch (IOException | ParseException | OpsGenieClientException e) {
            log.error("Failed to send OpsgenieNotification", e);
        }

    }

    @Override
    public Logger getLogger() {
        return log;
    }

}
