package org.moskito.control.ui.bean;

/**
 * Contains one element in the history view widget. A history event is a change in the component's health status.
 *
 * @author lrosenberg
 * @since 16.06.13 00:01
 */
public class HistoryItemBean {
	/**
	 * Time of the history event.
	 */
	private String time;
	/**
	 * Name of component that history change affected.
	 */
	private String componentName;
	/**
	 * Status before the change.
	 */
	private String oldStatus;
	/**
	 * Status after the change.
	 */
	private String newStatus;
	/**
	 * Associated messages.
	 */
	private String messages;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
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

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}
}
