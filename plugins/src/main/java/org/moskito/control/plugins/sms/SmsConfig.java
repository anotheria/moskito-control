package org.moskito.control.plugins.sms;


import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.plugins.notifications.config.BaseNotificationPluginConfig;

import java.util.Arrays;


/**
 * Sms config
 */
@ConfigureMe
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2", "EI_EXPOSE_REP"},
        justification = "This is the way configureMe works, it provides beans for access")
public class SmsConfig extends BaseNotificationPluginConfig<SmsNotificationConfig> {

    @Configure
    private String sid = "";

    @Configure
    private String authToken = "";

    /**
     * Phone number of 'sender'
     */
    @Configure
    private String phone = "";

    /**
     * WhatsApp phone number of 'sender'
     */
    @Configure
    private String waPhone = "";

    /**
     * Link to be included in the alert. Usually you will won't to link to moskito-control instance, for example:
     * https://<yourinstallation>/moskito-control/control/setApplication?application=${APPLICATION}
     */
    @Configure
    private String alertLink;

    /**
     * Notifications.
     */
    @Configure
    private SmsNotificationConfig[] notifications;

    public void setNotifications(SmsNotificationConfig[] notifications) {
        this.notifications = notifications;
    }
    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWaPhone() {
        return waPhone;
    }

    public void setWaPhone(String waPhone) {
        this.waPhone = waPhone;
    }

    public String getAlertLink() {
        return alertLink;
    }

    public void setAlertLink(String alertLink) {
        this.alertLink = alertLink;
    }

    @Override
    public String toString() {
        return "SmsConfig{" +
                "sid='" + sid + '\'' +
                ", authToken='" + authToken + '\'' +
                ", phone='" + phone + '\'' +
                ", waPhone='" + waPhone + '\'' +
                ", alertLink='" + alertLink + '\'' +
                ", notifications=" + Arrays.toString(notifications) +
                '}';
    }

    @Override
    protected SmsNotificationConfig[] getProfileConfigs() {
        return notifications;
    }
}
