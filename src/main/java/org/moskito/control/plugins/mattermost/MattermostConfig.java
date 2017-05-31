package org.moskito.control.plugins.mattermost;

import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.notifications.config.BaseNotificationPluginConfig;
import org.moskito.control.plugins.opsgenie.OpsgenieNotificationConfig;
import org.moskito.control.plugins.slack.SlackChannelConfig;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Configurable by ConfigureMe class,
 * witch represents Mattermost plugin config.
 */
@ConfigureMe
public class MattermostConfig extends BaseNotificationPluginConfig<MattermostChannelConfig>{

    /**
     * Mattermost site URL
     */
    @Configure
    private String host;
    /**
     * Username of mattermost user
     */
    @Configure
    private String username;
    /**
     * Password of mattermost user
     */
    @Configure
    private String password;
    /**
     * Mattermost team to send notifications
     */
    @Configure
    private String teamName;

    /**
     * Link to be included in the alert. Usually you will won't to link to moskito-control instance, for example:
     * https://<yourinstallation>/moskito-control/control/setApplication?application=${APPLICATION}
     */
    @Configure
    private String alertLink;

    /**
     * Base url path to thumb images for slack messages
     */
    @Configure
    private String baseImageUrlPath;

    /**
     * Configuration for slack channel
     * Links channel with applications
     */
    @Configure
    private MattermostChannelConfig[] channels;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAlertLink() {
        return alertLink;
    }

    public void setAlertLink(String alertLink) {
        this.alertLink = alertLink;
    }

    public String getBaseImageUrlPath() {
        return baseImageUrlPath;
    }

    public void setBaseImageUrlPath(String baseImageUrlPath) {
        this.baseImageUrlPath = baseImageUrlPath;
    }


    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setChannels(MattermostChannelConfig[] channels) {
        this.channels = channels;
    }

    @Override
    protected MattermostChannelConfig[] getProfileConfigs() {
        return channels;
    }

}
