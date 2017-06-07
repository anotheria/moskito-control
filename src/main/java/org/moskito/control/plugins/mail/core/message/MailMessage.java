package org.moskito.control.plugins.mail.core.message;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Wrapper for the message.
 *
 * @author Khilkevich Oleksii
 *
 */
public class MailMessage implements Serializable{

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 3390064209846182165L;

	/**
	 * Mail encoding
	 */
	private String encoding;

	/**
	 * Sender address of the message
	 */
	private String sender;

	/**
	 * Sender personal name of the message
	 */
	private String senderName = null;

	/**
	 * Subject of the message
	 */
	private String subject;

	/**
	 * Message body
	 */
	private String message;

	/**
	 * Recipients
	 */
	private String[] recipients;

	/**
	 * A map of addition headers
	 */
	private Map<String, String> headers = new HashMap<String, String>();

	/**
	 * Adds a header field to the message
	 *
	 * @param name name of the header
	 * @param value value of the header
	 */
	public void addHeader(String name, String value) {
		headers.put(name, value);
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String[] getRecipients() {
		return recipients;
	}

	public void setRecipients(String[] recipients) {
		this.recipients = recipients;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * This method is called to create a new java.mail.Message
	 *
	 * @param session the associated session
	 * @return mail message build from this object data
	 */
	public Message transformToMessage(Session session) throws MessagingException{
		Message msg = new MimeMessage(session);
	    msg.setFrom(new InternetAddress(sender));
        msg.setRecipients(Message.RecipientType.TO, prepareRecipients(recipients));
        msg.setContent((message != null ? message : ""), encoding);
		msg.setSubject((subject != null ? subject : ""));
		addHeadersToMessage(msg);
		return msg;
	}

    /**
     *
     * Builds array of {@link InternetAddress} from {@link String} emails array.
     *
     * @param addresses emails
     * @return emails as {@link InternetAddress} array
     * @throws AddressException when email parsing failed
     */
    private InternetAddress[] prepareRecipients(String[] addresses) throws AddressException {
        List<InternetAddress> recipients = new LinkedList<InternetAddress>();
        for (String address : addresses) {
            recipients.add(new InternetAddress(address));
        }
        return recipients.toArray(new InternetAddress[recipients.size()]);
    }

	/**
	 * Called to set headers in the message after transformation.
	 *
	 * @param msg
	 * @throws MessagingException
	 */
	private void addHeadersToMessage(Message msg) throws MessagingException {
		Collection<String> allHeaders = headers.keySet();
		for (String key : allHeaders) {
			String val = headers.get(key);
			msg.addHeader(key, val);
		}
	}
}