package org.moskito.control.plugins.slack;

import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

/**
 * Configuration for slack.
 * Config file defines by plugin config
 */
@ConfigureMe
public class SlackConfig {

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
    private String channel;

    /**
     * Base url path to thumb images for slack messages
     */
    @Configure
    private String baseImageUrlPath;

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
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
}
