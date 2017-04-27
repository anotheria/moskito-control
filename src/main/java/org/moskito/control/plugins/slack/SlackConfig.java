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
     * Channel name to send status change messages
     */
    @Configure
    private String channel;

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

}
