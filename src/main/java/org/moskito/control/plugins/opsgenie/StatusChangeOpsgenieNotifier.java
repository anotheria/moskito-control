package org.moskito.control.plugins.opsgenie;

import com.ifountain.opsgenie.client.OpsGenieClient;
import com.ifountain.opsgenie.client.OpsGenieClientException;
import com.ifountain.opsgenie.client.model.alert.CreateAlertRequest;
import com.ifountain.opsgenie.client.model.alert.CreateAlertResponse;
import net.anotheria.util.NumberUtils;
import org.moskito.control.core.notification.AbstractStatusChangeNotifier;
import org.moskito.control.core.status.StatusChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Optional;

/**
 * Status change OpsGenie notifier.
 * Sends alerts to OpsGenie on any component status change
 */
public final class StatusChangeOpsgenieNotifier extends AbstractStatusChangeNotifier {

    /**
     * Configuration of OpsGenie notifications
     */
    private OpsgenieConfig config;

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(StatusChangeOpsgenieNotifier.class);

    public StatusChangeOpsgenieNotifier(OpsgenieConfig config) {
        this.config = config;
    }

    /**
     * Builds alert description string.
     * @param event status change event, source of data for description
     * @return status change event description
     */
    private String buildDescription(StatusChangeEvent event){
        return  "Timestamp: " + NumberUtils.makeISO8601TimestampString((event.getTimestamp())) + "\n"
                + "Application: " + event.getApplication() + "\n"
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
               " to " + event.getStatus().getHealth() + " in application " + event.getApplication()
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
    public void notifyStatusChange(StatusChangeEvent event) {

        log.debug("Processing via opsgenie notifier status change event: {}", event);

        OpsGenieClient client = new OpsGenieClient();
        Optional<OpsgenieNotificationConfig> notificationConfig = config.getProfileForEvent(event);

        if(!notificationConfig.isPresent()){
            log.info("No notification config found for event {}", event);
            return;
        }

        try {

            CreateAlertResponse response = client.alert().createAlert(
                    createAlertRequest(event, notificationConfig.get())
            );

            String alertId = response.getId();

            log.warn(
                    "OpsGenie notification was send for status change event: {} with alertId {}", event, alertId
            );

        } catch (IOException | ParseException | OpsGenieClientException e) {
            log.error("Failed to send OpsgenieNotification", e);
        }

    }

}
