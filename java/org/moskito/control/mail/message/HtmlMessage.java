package org.moskito.control.mail.message;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author Khilkevich Oleksii
 */
public class HtmlMessage extends AbstractMailMessage{

	/**
	 *  Content type.
	 */
	private static final String ENCODING ="text/html; charset=UTF-8";

	@Override
	public Message transformToMessage(Session session) throws MessagingException {

		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(getSender()));
		InternetAddress receiver = new InternetAddress(getRecipient());
		msg.setRecipient(Message.RecipientType.TO, receiver);
		msg.setSubject((getSubject() != null ? getSubject() : ""));
		msg.setContent((getMessage() != null ? getMessage() : ""), ENCODING);
		return msg;
	}

}
