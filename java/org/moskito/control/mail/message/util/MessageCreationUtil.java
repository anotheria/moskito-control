package org.moskito.control.mail.message.util;

import org.moskito.control.mail.MailServiceConfig;
import org.moskito.control.mail.message.MailMessage;

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
     * @return
	 */
	public static MailMessage createHtmlMailMessage(String[] recipients, String content, String subject){
		return createDefaultMessage(recipients, content, subject, HTML_ENCODING);
	}

	/**
	 * Creates HtmlMailMessage.
	 *
     * @param recipients recipients
     * @param content content
     * @param subject subject
     * @return
	 */
	public static MailMessage createSimpleMailMessage(String[] recipients, String content, String subject){
		return createDefaultMessage(recipients, content, subject, PLAIN_TEXT_ENCODING);
	}

	/**
	 * Creates default message.
	 *
     * @param recipients recipients
     * @param content content
     * @param subject subject
     * @param encoding encoding
     * @return
	 */
	private static MailMessage createDefaultMessage(String[] recipients, String content, String subject, String encoding){
		MailMessage message = new MailMessage();
		message.setMessage(content);
		message.setRecipients(recipients);
		message.setSender(MailServiceConfig.getInstance().getDefaultMessageSender());
		message.setSubject(subject);
		message.setEncoding(encoding);
		return message;
	}


}
