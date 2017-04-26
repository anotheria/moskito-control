package org.moskito.control.config.notifiers.slack;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

/**
 * Configuration for slack
 */
@ConfigureMe(name="slack")
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

    /**
     * Instance of SlackConfig.
     */
    private static SlackConfig instance = new SlackConfig();

    /**
     * Constructor.
     */
    private SlackConfig(){
        ConfigurationManager.INSTANCE.configure(this);
    }

    /**
     * @return Instance of config
     */
    public static SlackConfig getInstance(){
        return instance;
    }

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
