package org.moskito.control.mail.message;

import net.anotheria.util.NumberUtils;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.mail.MailServiceConfig;
import org.moskito.control.mail.message.util.MessageCreationUtil;

/**
 * Message builder for status changed mail.
 *
 * @author Khilkevich Oleksii
 */
public class MailMessageBuilder {

	public static MailMessage buildStatusChangedMessage(StatusChangeEvent event, String[] recipients){
		String content = "Timestamp: <b>" + NumberUtils.makeISO8601TimestampString((event.getTimestamp())) + "</b><br/>"
				+ "Application: <b>" + event.getApplication() + "</b><br/>"
				+ "Component: <b>" + event.getComponent() + "</b><br/>"
				+ "Old status: <b>" + event.getOldStatus() + "</b><br/>"
				+ "New status: <b>" + event.getStatus() + "</b><br/>";
		return  MessageCreationUtil.createHtmlMailMessage(recipients, content, MailServiceConfig.getInstance().getDefaultMessageSubject());
	}
}
