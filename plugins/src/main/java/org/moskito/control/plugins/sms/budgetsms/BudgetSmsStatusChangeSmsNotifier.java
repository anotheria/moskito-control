package org.moskito.control.plugins.sms.budgetsms;

import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.notifications.AbstractStatusChangeNotifier;
import org.moskito.control.plugins.sms.budgetsms.util.ParameterStringBuilder;
import org.moskito.control.plugins.sms.twilio.SmsContentBuilder;
import org.moskito.control.plugins.sms.twilio.StatusChangeSmsNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BudgetSmsStatusChangeSmsNotifier extends AbstractStatusChangeNotifier<BudgetSmsMessagingNotificationConfig> {

    /**
     * Configuration for sms notifications
     */
    private BudgetSmsMessagingConfig config;

    /**
     * Sets configuration
     * @param config configuration for notifications
     */
    BudgetSmsStatusChangeSmsNotifier(BudgetSmsMessagingConfig config) {
        super(config);
        this.config = config;
    }

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(StatusChangeSmsNotifier.class);

    @Override
    public void notifyStatusChange(StatusChangeEvent event, BudgetSmsMessagingNotificationConfig profile) {

        String content = new SmsContentBuilder()
                .setEvent(event)
                .setAlertLinkTemplate(config.getAlertLink())
                .build();

        for (String recipient: profile.getRecipients()) {
            sendSMS(recipient, content);
        }

        log.info("Notification core was send for status change event: {}", event);
    }

    @Override
    public Logger getLogger() {
        return log;
    }

    private void sendSMS(String to, String content){
        try{
            URL url = new URL("https://api.budgetsms.net/sendsms/");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            Map<String, String> parameters = new HashMap<>();
            parameters.put("username", config.getUsername());
            parameters.put("userid", config.getUserid());
            parameters.put("handle", config.getHandle());
            parameters.put("msg", content);
            parameters.put("from", config.getSender());
            parameters.put("to", to);

            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
            out.flush();
            out.close();

            String response = readResponse(con);

            if(response.startsWith("ERR")){
                log.error("Error sending SMS via BudgetSMS: message - {}, receiver number - {}", response, to);
            }

            con.disconnect();
        } catch (IOException ex){
            log.error("Unable to send SMS via BudgetSMS: {}", ex, ex);
        }
    }

    private String readResponse(HttpURLConnection con) throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

}

