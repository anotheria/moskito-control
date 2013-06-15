package org.moskito.control.ui.resource;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Represents a single element in a rest reply.
 *
 * @author lrosenberg
 * @since 13.06.13 17:02
 */
@XmlRootElement
public class HistoryItemBean {
	@XmlElement
	private long timestamp;

	@XmlElement
	private String isoTimestamp;

	@XmlElement
	private String oldStatus;

	@XmlElement
	private String newStatus;

	@XmlElement
	private String componentName;


	@XmlElement
	private List<String> oldMessages;

	@XmlElement
	private List<String> newMessages;

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getIsoTimestamp() {
		return isoTimestamp;
	}

	public void setIsoTimestamp(String isoTimestamp) {
		this.isoTimestamp = isoTimestamp;
	}

	public String getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}

	public String getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public List<String> getOldMessages() {
		return oldMessages;
	}

	public void setOldMessages(List<String> oldMessages) {
		this.oldMessages = oldMessages;
	}

	public List<String> getNewMessages() {
		return newMessages;
	}

	public void setNewMessages(List<String> newMessages) {
		this.newMessages = newMessages;
	}
}
