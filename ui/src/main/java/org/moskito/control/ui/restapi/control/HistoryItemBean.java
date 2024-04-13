package org.moskito.control.ui.restapi.control;

import io.swagger.v3.oas.annotations.media.Schema;
import net.anotheria.util.NumberUtils;
import org.moskito.control.core.history.StatusUpdateHistoryItem;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Represents a single element in a rest reply.
 *
 * @author lrosenberg
 * @since 13.06.13 17:02
 */
@Schema(name = "HistoryItemBean", description = "A single entry in the history. Represents a status change of a component.")
public class HistoryItemBean {
	/**
	 * Timestamp of the change.
	 */
	private long timestamp;

	/**
	 * Timestamp of the change as iso-8661 timestamp (human readable).
	 */
	private String isoTimestamp;

	/**
	 * StatusResource prior to the change.
	 */
	private String oldStatus;

	/**
	 * StatusResource after the change.
	 */
	private String newStatus;

	/**
	 * Name of the affected component.
	 */
	private String componentName;


	/**
	 * Messages in the old state.
	 */
	private List<String> oldMessages;

	/**
	 * Messages in the new state.
	 */
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

	public static HistoryItemBean fromStatusUpdateHistoryItem(StatusUpdateHistoryItem item) {
		HistoryItemBean result = new HistoryItemBean();
		result.setComponentName(item.getComponent().getName());
		result.setIsoTimestamp(NumberUtils.makeISO8601TimestampString(item.getTimestamp()));
		result.setTimestamp(item.getTimestamp());
		result.setOldStatus(item.getOldStatus().getHealth().name());
		result.setNewStatus(item.getNewStatus().getHealth().name());
		result.setOldMessages(item.getOldStatus().getMessages());
		result.setNewMessages(item.getNewStatus().getMessages());
		return result;
	}
}
