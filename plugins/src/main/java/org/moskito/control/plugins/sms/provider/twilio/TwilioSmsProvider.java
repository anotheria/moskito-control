package org.moskito.control.plugins.sms.provider.twilio;

import com.twilio.Twilio;
import com.twilio.exception.TwilioException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.moskito.control.plugins.sms.SmsConfig;
import org.moskito.control.plugins.sms.provider.SmsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwilioSmsProvider implements SmsProvider {

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(TwilioSmsProvider.class);

    private SmsConfig config;

    private final static String WHATSAPP_TWILIO_PREFIX = "whatsapp:";

    public TwilioSmsProvider(SmsConfig config) {
        this.config = config;
        Twilio.init(config.getSid(), config.getAuthToken());
    }

    @Override
    public void send(String phone, String content){
        try {
            if(phone.startsWith(WHATSAPP_TWILIO_PREFIX)){
                Message.creator(new PhoneNumber(phone), new PhoneNumber(WHATSAPP_TWILIO_PREFIX + config.getWaPhone()), content).create();
            } else {
                Message.creator(new PhoneNumber(phone), new PhoneNumber(config.getPhone()), content).create();
            }
        } catch (TwilioException ex) {
            log.error("Failed to send Twilio notification", ex);
        }
    }
}
