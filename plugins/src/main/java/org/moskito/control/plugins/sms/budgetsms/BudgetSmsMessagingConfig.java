package org.moskito.control.plugins.sms.budgetsms;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.plugins.notifications.config.BaseNotificationPluginConfig;

import java.util.Arrays;

@ConfigureMe
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2", "EI_EXPOSE_REP"},
        justification = "This is the way configureMe works, it provides beans for access")
public class BudgetSmsMessagingConfig extends BaseNotificationPluginConfig<BudgetSmsMessagingNotificationConfig> {

    /**
     * BudgetSMS username
     */
    @Configure
    private String username = "";

    /**
     * BudgetSMS userid
     */
    @Configure
    private String userid = "";

    /**
     * BudgetSMS unique identifier
     */
    @Configure
    private String handle = "";

    @Configure
    private String sender = "";

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
    private BudgetSmsMessagingNotificationConfig[] notifications;

    public void setNotifications(BudgetSmsMessagingNotificationConfig[] notifications) {
        this.notifications = notifications;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getAlertLink() {
        return alertLink;
    }

    public void setAlertLink(String alertLink) {
        this.alertLink = alertLink;
    }

    @Override
    public String toString() {
        return "AlternateSmsConfig{" +
                "username='" + username + '\'' +
                ", userid='" + userid + '\'' +
                ", handle='" + handle + '\'' +
                ", sender='" + sender + '\'' +
                ", alertLink='" + alertLink + '\'' +
                ", notifications=" + Arrays.toString(notifications) +
                '}';
    }

    @Override
    protected BudgetSmsMessagingNotificationConfig[] getProfileConfigs() {
        return notifications;
    }
}

