package org.moskito.control.core.history;

import net.anotheria.util.NumberUtils;
import org.moskito.control.core.Component;
import org.moskito.control.core.Status;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 09.06.13 22:14
 */
public class StatusUpdateHistoryItem {
	private long timestamp;
	private Component component;
	private Status oldStatus;
	private Status newStatus;

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
