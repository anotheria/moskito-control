package org.moskito.control.mail.message;

/**
 * @author Khilkevich Oleksii
 */
public class MessageCreationUtil {


	/**
	 *  HTML content type.
	 */
	private static final String HTML_ENCODING ="text/html; charset=UTF-8";

	/**
	 *  Content type.
	 */
	private static final String PLAIN_TEXT_ENCODING ="text/plain; charset=UTF-8";

	/**
	 * Creates HtmlMailMessage.
	 * @param recipient recipient
	 * @param content content
	 * @param sender  sender
	 * @param subject subject
	 * @return
	 */
	public static MailMessage createHtmlMailMessage(String recipient, String content,
													String sender, String subject){
		return createDefaultMessage(recipient, content, sender, subject, HTML_ENCODING);
	}

	/**
	 * Creates HtmlMailMessage.
	 * @param recipient recipient
	 * @param content content
	 * @param sender  sender
	 * @param subject subject
	 * @return
	 */
	public static MailMessage createSimpleMailMessage(String recipient, String content,
															String sender, String subject){
		return createDefaultMessage(recipient, content, sender, subject, PLAIN_TEXT_ENCODING);
	}

	/**
	 *  Creates default message.
	 * @param recipient recipient
	 * @param content content
	 * @param sender  sender
	 * @param subject  subject
	 * @param encoding  encoding
	 * @return
	 */
	private static MailMessage createDefaultMessage(String recipient, String content,
															String sender, String subject,
															String encoding){
		MailMessage message = new MailMessage();
		message.setMessage(content);
		message.setRecipient(recipient);
		message.setSender(sender);
		message.setSubject(subject);
		message.setEncoding(encoding);
		return message;
	}


}
