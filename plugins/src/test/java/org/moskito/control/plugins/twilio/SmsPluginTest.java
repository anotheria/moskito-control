package org.moskito.control.plugins.twilio;

import org.configureme.ConfigurationManager;
import org.junit.Test;
import org.moskito.control.common.HealthColor;
import org.moskito.control.common.Status;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.notifications.config.NotificationStatusChange;
import org.moskito.control.plugins.sms.budgetsms.util.ParameterStringBuilder;
import org.moskito.control.plugins.sms.twilio.TwilioMessagingConfig;
import org.moskito.control.plugins.sms.twilio.TwilioMessagingNotificationConfig;
import org.moskito.control.plugins.sms.twilio.TwilioMessagingProvider;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SmsPluginTest {

    @Test
    public void smsBTest(){
        try{
            URL url = new URL("https://api.budgetsms.net/testsms/");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            Map<String, String> parameters = new HashMap<>();
            parameters.put("username", "pup");
            parameters.put("userid", "pup");
            parameters.put("handle", "pup");
            parameters.put("msg", "pup");
            parameters.put("from", "pup");
            parameters.put("to", "pup");

            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
            out.flush();
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            if(response.toString().startsWith("ERR")){
                System.out.println("testPassed");
            }

            con.disconnect();
        } catch (IOException ex){
            System.out.println("Unable to send SMS via BudgetSMS");
        }
    }

    @Test
    public void configTest() {
        TwilioMessagingConfig config = new TwilioMessagingConfig();
        ConfigurationManager.INSTANCE.configureAs(config, "sms");

        assertNotNull(config.getSid());
        assertNotNull(config.getAuthToken());
        assertNotNull(config.getPhone());
        assertNotNull(config.getWaPhone());
        assertNotNull(config.getAlertLink());
    }

    @Test
    public void sendSmsTest() {
        TwilioMessagingConfig config = new TwilioMessagingConfig();
        ConfigurationManager.INSTANCE.configureAs(config, "sms");
        TwilioMessagingProvider provider = new TwilioMessagingProvider(config);

        provider.send("+380555555555", "TEST");
    }

    @Test
    public void sendWhatsAppSmsTest() {
        TwilioMessagingConfig config = new TwilioMessagingConfig();
        ConfigurationManager.INSTANCE.configureAs(config, "sms");
        TwilioMessagingProvider provider = new TwilioMessagingProvider(config);

        provider.send("whatsapp:+380555555555", "Monolithic");
    }

    @Test
    public void testApplicableIfMatchedStatus(){
        TwilioMessagingNotificationConfig config = new TwilioMessagingNotificationConfig();
        config.setNotificationStatusChanges(
                new NotificationStatusChange[]{
                        new NotificationStatusChange(
                                new String[]{"RED", "ORANGE"},
                                null)
                });
        assertTrue(config.isAppliableToEvent(createEvent( HealthColor.RED)));
    }

    @Test
    public void testNonApplicableIfNotMatchedStatus(){
        TwilioMessagingNotificationConfig config = new TwilioMessagingNotificationConfig();
        config.setNotificationStatusChanges(
                new NotificationStatusChange[]{
                        new NotificationStatusChange(
                                new String[]{"RED", "YELLOW"},
                                null)
                });
        assertFalse(config.isAppliableToEvent(createEvent( HealthColor.GREEN)));
    }

    private StatusChangeEvent createEvent(HealthColor color){
        StatusChangeEvent event = new StatusChangeEvent();
        event.setStatus(new Status(color, ""));
        event.setOldStatus(new Status(HealthColor.GREEN, ""));
        return event;
    }
}
