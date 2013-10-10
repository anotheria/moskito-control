package org.moskito.control.mail.message;

import org.moskito.control.core.StatusChangeEvent;
import org.moskito.control.mail.MailServiceConfig;
import org.moskito.control.mail.message.util.MessageCreationUtil;

import java.util.Date;

/**
 * Message builder for status changed mail.
 *
 * @author Khilkevich Oleksii
 */
public class MailMessageBuilder {

	public static MailMessage buildStatusChangedMessage(StatusChangeEvent event, String recipient){
		String content = "<b>"
				+ new Date(event.getTimestamp())
				+ "</b>: Application <b>"
				+ event.getApplication()
				+ "</b> component <b>"
				+ event.getComponent()
				+ "</b> has changed status from <b>"
				+ event.getOldStatus()
				+ "</b> to <b>"
				+ event.getStatus()
				+ "</b>";
		return  MessageCreationUtil.createHtmlMailMessage(recipient, content, MailServiceConfig.getInstance().getDefaultMessageSubject());
	}
}
