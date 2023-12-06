package org.moskito.control.plugins.sms.provider;

import org.moskito.control.plugins.sms.SmsConfig;
import org.moskito.control.plugins.sms.SmsProviderType;
import org.moskito.control.plugins.sms.provider.twilio.TwilioSmsProvider;

public class SmsProviderFactory {

    public static SmsProvider getProvider(SmsProviderType provider, SmsConfig config) {
        switch (provider) {
            case TWILIO:
                return new TwilioSmsProvider(config);
            default:
                throw new UnsupportedOperationException("Unsupported provider: " + provider);
        }
    }
}
