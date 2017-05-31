package org.moskito.control.plugins.slack;

import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.notifications.config.BaseNotificationPluginConfig;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Configuration for slack.
 * Config file defines by plugin config
 */
@ConfigureMe
public class SlackConfig extends BaseNotificationPluginConfig<SlackChannelConfig> {

    /**
     * Bot token
     */
    @Configure
    private String botToken;

	/**
	 * Link to be included in the alert. Usually you will won't to link to moskito-control instance, for example:
	 * https://<yourinstallation>/moskito-control/control/setApplication?application=${APPLICATION}
	 */
	@Configure
	private String alertLink;

    /**
     * Channel name to send status change messages
     */
    @Configure
    private String defaultChannel;

    /**
     * Base url path to thumb images for slack messages
     */
    @Configure
    private String baseImageUrlPath;

    /**
     * Configuration for slack channel
     * Links channel with applications
     * By default empty array to prevent null pointer exception
     */
    @Configure
    private SlackChannelConfig[] channels = new SlackChannelConfig[0];

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public String getDefaultChannel() {
        return defaultChannel;
    }

    public void setDefaultChannel(String defaultChannel) {
        this.defaultChannel = defaultChannel;
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

    public void setChannels(SlackChannelConfig[] channels) {
        this.channels = channels;
    }

    /**
     * Returns list of registered in slack config channels
     * @return list of channel names
     */
    public List<String> getRegisteredChannels(){
        return Arrays.stream(channels)
                .map(SlackChannelConfig::getName)
                .collect(Collectors.toList());
    }

	@Override
	public String toString() {
		return "SlackConfig{" +
				"botToken='" + botToken + '\'' +
				", alertLink='" + alertLink + '\'' +
				", defaultChannel='" + defaultChannel + '\'' +
				", baseImageUrlPath='" + baseImageUrlPath + '\'' +
				", channels=" + Arrays.toString(channels) +
				'}';
	}

    @Override
    protected SlackChannelConfig[] getProfileConfigs() {
        return channels;
    }
}
