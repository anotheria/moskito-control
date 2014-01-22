package org.moskito.control.mail;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.AfterReConfiguration;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.core.HealthColor;

import java.util.HashMap;
import java.util.Map;

/**
 * Config for the mail service.
 *
 * @author Khilkevich Oleksii
 */
@ConfigureMe(name="mail")
public final class MailServiceConfig {

	/**
	 * Notifications.
	 */
	@Configure
	private NotificationConfig[] notifications;

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
     * Array of mail notification recipients per application status.
     */
    private Map<HealthColor, String[]> notificationsMap;

	/**
	 * Constructor.
	 */
	private MailServiceConfig(){
		ConfigurationManager.INSTANCE.configure(this);
	}

    @AfterConfiguration
    @AfterReConfiguration
    public void updateNotificationsMap() {
        notificationsMap = new HashMap<HealthColor, String[]>();
        for (NotificationConfig notification : notifications) {
            notificationsMap.put(notification.getGuardedStatus(), notification.getRecipients());
        }
    }

	public String getConfigurationName() {
		return "mail";
	}

	public String toString(){
		return getUser()+"!"+getPassword()+":"+getHost()+" - "+isDebug();
	}

    public NotificationConfig[] getNotifications() {
        return notifications;
    }

    public void setNotifications(NotificationConfig[] notifications) {
        this.notifications = notifications;
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

	public static MailServiceConfig getInstance(){
		return instance;
	}

    public Map<HealthColor, String[]> getNotificationsMap() {
        return notificationsMap;
    }
}
