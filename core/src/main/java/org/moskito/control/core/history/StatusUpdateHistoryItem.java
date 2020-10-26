package org.moskito.control.core.history;

import net.anotheria.util.NumberUtils;
import org.moskito.control.common.Status;
import org.moskito.control.core.Component;

/**
 * Internal holder class for status updates.
 *
 * @author lrosenberg
 * @since 09.06.13 22:14
 */
public class StatusUpdateHistoryItem {
	/**
	 * Timestamp of the change.
	 */
	private long timestamp;
	/**
	 * Affected component.
	 */
	private Component component;
	/**
	 * Status before the change.
	 */
	private Status oldStatus;
	/**
	 * Status after the change.
	 */
	private Status newStatus;

	/**
	 * Creates a new StatusUpdateHistoryItem.
	 * @param component affected component.
	 * @param oldStatus status before the change.
	 * @param status status after the change.
	 * @param updateTimestamp timestamp of the update.
	 */
	public StatusUpdateHistoryItem(Component component, Status oldStatus, Status status, long updateTimestamp){
		timestamp = updateTimestamp;
		this.component = component;
		this.oldStatus = oldStatus;
		this.newStatus = status;
	}

	@Override public String toString(){
		return NumberUtils.makeISO8601TimestampString(timestamp)+" "+component.getName()+" "+oldStatus+" -> "+newStatus;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public Status getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(Status oldStatus) {
		this.oldStatus = oldStatus;
	}

	public Status getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(Status newStatus) {
		this.newStatus = newStatus;
	}
}
