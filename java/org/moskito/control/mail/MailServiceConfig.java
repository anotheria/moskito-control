package org.moskito.control.mail;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

/**
 * Config for the mail service.
 *
 * @author Khilkevich Oleksii
 */
@ConfigureMe(name="mail")
public final class MailServiceConfig {

	/**
	 * Recipient.
	 */
	@Configure
	private String[] recipients;

	/**
	 * Host.
	 */
	@Configure
	private String host;

    /**
     * SMTP port.
     */
    @Configure
    private int port;

	/**
	 * User.
	 */
	@Configure
	private String user;

	/**
	 * Password.
	 */
	@Configure
	private String password;

    /**
     * Message sender field in change status message.
     */
    @Configure
    private String defaultMessageSender;

	/**
	 * Default message subject.
	 */
	@Configure
	private String defaultMessageSubject;

	/**
	 *  Is debug enabled.
	 */
	@Configure
	private boolean debug;

	/**
	 * Instance of MailServiceConfig.
	 */
	private static MailServiceConfig instance = new MailServiceConfig();

	/**
	 * Constructor.
	 */
	private MailServiceConfig(){
		ConfigurationManager.INSTANCE.configure(this);
	}

	public String getConfigurationName() {
		return "mail";
	}

	public String toString(){
		return getUser()+"!"+getPassword()+":"+getHost()+" - "+isDebug();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

    public String getDefaultMessageSender() {
        return defaultMessageSender;
    }

    public void setDefaultMessageSender(String defaultMessageSender) {
        this.defaultMessageSender = defaultMessageSender;
    }

	public String getDefaultMessageSubject() {
		return defaultMessageSubject;
	}

	public void setDefaultMessageSubject(String defaultMessageSubject) {
		this.defaultMessageSubject = defaultMessageSubject;
	}

	public String[] getRecipients() {
		return recipients;
	}

	public void setRecipients(String[] recipients) {
		this.recipients = recipients;
	}

	public static MailServiceConfig getInstance(){
		return instance;
	}

}
