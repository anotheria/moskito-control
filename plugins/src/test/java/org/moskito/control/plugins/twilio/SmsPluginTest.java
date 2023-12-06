package org.moskito.control.plugins.twilio;

import org.checkerframework.checker.units.qual.C;
import org.configureme.ConfigurationManager;
import org.junit.Test;
import org.moskito.control.common.HealthColor;
import org.moskito.control.common.Status;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.notifications.config.NotificationStatusChange;
import org.moskito.control.plugins.sms.SmsConfig;
import org.moskito.control.plugins.sms.SmsNotificationConfig;
import org.moskito.control.plugins.sms.provider.twilio.TwilioSmsProvider;

import static org.junit.Assert.*;

public class SmsPluginTest {

    @Test
    public void configTest() {
        SmsConfig config = new SmsConfig();
        ConfigurationManager.INSTANCE.configureAs(config, "sms");

        assertNotNull(config.getSid());
        assertNotNull(config.getAuthToken());
        assertNotNull(config.getPhone());
        assertNotNull(config.getWaPhone());
        assertNotNull(config.getAlertLink());
    }

    @Test
    public void sendSmsTest() {
        SmsConfig config = new SmsConfig();
        ConfigurationManager.INSTANCE.configureAs(config, "sms");
        TwilioSmsProvider provider = new TwilioSmsProvider(config);

        provider.send("+380555555555", "TEST");
    }

    @Test
    public void sendWhatsAppSmsTest() {
        SmsConfig config = new SmsConfig();
        ConfigurationManager.INSTANCE.configureAs(config, "sms");
        TwilioSmsProvider provider = new TwilioSmsProvider(config);

        provider.send("whatsapp:+380555555555", "Monolithic");
    }

    @Test
    public void testApplicableIfMatchedStatus(){
        SmsNotificationConfig config = new SmsNotificationConfig();
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
        SmsNotificationConfig config = new SmsNotificationConfig();
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
