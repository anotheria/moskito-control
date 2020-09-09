package org.moskito.control.plugins.mail.core.message.util;

import org.moskito.control.plugins.mail.core.message.MailMessage;

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
	 *
     * @param recipients recipients
     * @param content content
     * @param subject subject
     * @return new mail message with html encoding
	 */
	public static MailMessage createHtmlMailMessage(String[] recipients, String sender, String content, String subject){
		return createDefaultMessage(recipients, sender, content, subject, HTML_ENCODING);
	}

	/**
	 * Creates HtmlMailMessage.
	 *
     * @param recipients recipients
     * @param content content
     * @param subject subject
     * @return new mail message with plain text encoding
	 */
	public static MailMessage createSimpleMailMessage(String[] recipients, String sender, String content, String subject){
		return createDefaultMessage(recipients, sender, content, subject, PLAIN_TEXT_ENCODING);
	}

	/**
	 * Creates default message.
	 *
     * @param recipients recipients
     * @param content content
     * @param subject subject
     * @param encoding encoding
     * @return new mail message with defined by method arguments parameters
	 */
	private static MailMessage createDefaultMessage(String[] recipients, String sender, String content, String subject, String encoding){
		MailMessage message = new MailMessage();
		message.setMessage(content);
		message.setRecipients(recipients);
		message.setSender(sender);
		message.setSubject(subject);
		message.setEncoding(encoding);
		return message;
	}


}
