package org.moskito.control.plugins.monitoring.mail;

import com.google.gson.annotations.SerializedName;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

import java.util.Arrays;

@ConfigureMe(name = "plugin-monitoring-mail")
public class MonitoringMailPluginConfig {

    /**
     * Interval in minutes between fetch mail messages.
     */
    @Configure
    private int fetchIntervalMinutes = 5;

    @Configure
    private int sendIntervalMinutes = 10;

    @Configure
    @SerializedName("@mailConfigs")
    private MonitoringMailConfig[] mailConfigs;

    public int getFetchIntervalMinutes() {
        return fetchIntervalMinutes;
    }

    public void setFetchIntervalMinutes(int fetchIntervalMinutes) {
        this.fetchIntervalMinutes = fetchIntervalMinutes;
    }

    public int getSendIntervalMinutes() {
        return sendIntervalMinutes;
    }

    public void setSendIntervalMinutes(int sendIntervalMinutes) {
        this.sendIntervalMinutes = sendIntervalMinutes;
    }

    public MonitoringMailConfig[] getMailConfigs() {
        return mailConfigs;
    }

    public void setMailConfigs(MonitoringMailConfig[] mailConfigs) {
        this.mailConfigs = mailConfigs;
    }

    public static final MonitoringMailPluginConfig getByName(String name){
        MonitoringMailPluginConfig ret = new MonitoringMailPluginConfig();
        ConfigurationManager.INSTANCE.configureAs(ret, name);
        return ret;
    }

    @Override
    public String toString() {
        return "MonitoringMailPluginConfig{" +
                "fetchIntervalMinutes=" + fetchIntervalMinutes +
                ", sendIntervalMinutes=" + sendIntervalMinutes +
                ", mailConfigs=" + Arrays.toString(mailConfigs) +
                '}';
    }
}
