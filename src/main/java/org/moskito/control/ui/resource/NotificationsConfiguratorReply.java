package org.moskito.control.ui.resource;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The reply object for the {@link NotificationsConfiguratorResource}.
 * @author strel
 */
@XmlRootElement
public class NotificationsConfiguratorReply extends ControlReplyObject {

	/**
	 * The result of notifications toggle.
	 */
	@XmlElement
	private boolean result;


	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}
}
