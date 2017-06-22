package org.moskito.control.plugins.mail.core;

import org.moskito.control.plugins.mail.MailServiceConfig;
import org.moskito.control.plugins.mail.core.message.MailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import java.util.Properties;

/**
 * Mail service.
 *
 * @author Khilkevich Oleksii
 */
public final class MailService {

	/**
	 * Log.
	 */
	private static Logger log = LoggerFactory.getLogger(MailService.class);

	/**
	 * Session.
	 */
	private Session mailSession;

	/**
	 * Mail service config.
	 */
	private MailServiceConfig config;

	/**
	 * Constructor.
	 */
	public MailService(MailServiceConfig config){
		Properties props = new Properties();
		this.config = config;
		props.put("mail.smtp.host", config.getHost());
		props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", String.valueOf(config.getPort()));
		props.put("mail.debug", config.isDebug());
		mailSession = Session.getInstance(props, new SMTPAuthenticator());
		mailSession.setDebug(config.isDebug());
	}

	public boolean send(MailMessage message){
		try {
			Transport.send(message.transformToMessage(mailSession));
		} catch(MessagingException e) {
			log.error("deliverMailMessage message :{"+message.toString()+"}", e);
		}
	   return true;
	}

	private class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			String username = config.getUser();
			String password = config.getPassword();
			return new PasswordAuthentication(username, password);
		}
	}


}
