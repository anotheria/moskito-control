
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
public class MailServiceConfig {

	/**
	 * Host.
	 */
	@Configure
	private String host;

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
	 *  Is debug enabled.
	 */
	@Configure
	private boolean debug;

	public MailServiceConfig(){
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

}
