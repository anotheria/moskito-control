package org.moskito.control.plugins.sms.provider;

public interface SmsProvider {

    void send(String phone, String content);

}
