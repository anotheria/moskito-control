package org.moskito.control.ui.resource;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Generic reply object for control rest interface.
 *
 * @author lrosenberg
 * @since 13.06.13 17:11
 */
@XmlRootElement
public class ControlReplyObject {
	/**
	 * Version of the protocol, a constant allowing the counterpart to choose a specific parser.
	 */
	@XmlElement
	private int protocolVersion = 1;

	/**
	 * Timestamp the reply was generated.
	 */
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
