package org.moskito.control.mail;



import net.anotheria.communication.exceptions.MessageDeliverException;
import org.configureme.ConfigurationManager;
import org.moskito.control.mail.message.AbstractMailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import java.util.Properties;

/**
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
	 * Singleton instance.
	 */
	private static MailService instance = new MailService();

	/**
	 * Lock public constructor.
	 */
	private MailService(){
		Properties props = new Properties();
		config = new MailServiceConfig();
		props.put("mail.smtp.host", config.getHost());
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", config.isDebug());
		props.put("mail.user", config.getUser());
		props.put("mail.password", config.getPassword());
		mailSession = Session.getInstance(props);
		mailSession.setDebug(config.isDebug());
	}

	public static MailService getInstance(){
		return instance;
	}

	public boolean send(AbstractMailMessage message){
		try {
			Transport.send(message.transformToMessage(mailSession));
		} catch(AddressException e) {
			log.error("deliverMailMessage message :{"+message.toString()+"}", e);
		} catch(MessagingException e) {
			log.error("deliverMailMessage message :{"+message.toString()+"}", e);
		}
	   return true;
	}


}
