package org.moskito.control.mail.message;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Khilkevich Oleksii
 */
public abstract class AbstractMailMessage implements Serializable{

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 3390064209846182165L;

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
	 * Reply-to header field
	 */
	private String replyTo;

	/**
	 * Recipient
	 */
	private String recipient;

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

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	/**
	 * This method is called to create a new java.mail.Message
	 *
	 * @param session the associated session
	 * @return
	 */
	public abstract Message transformToMessage(Session session) throws MessagingException;


}
