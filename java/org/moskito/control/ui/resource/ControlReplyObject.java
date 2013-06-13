package org.moskito.control.ui.resource;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 13.06.13 17:11
 */
@XmlRootElement
public class ControlReplyObject {
	@XmlElement
	private int protocolVersion = 1;

	@XmlElement
	private long currentServerTimestamp = System.currentTimeMillis();

	public int getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public long getCurrentServerTimestamp() {
		return currentServerTimestamp;
	}

	public void setCurrentServerTimestamp(long currentServerTimestamp) {
		this.currentServerTimestamp = currentServerTimestamp;
	}
}
