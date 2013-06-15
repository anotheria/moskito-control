package org.moskito.control.ui.bean;

import java.util.List;

/**
 * Represents a single component in the view.
 *
 * @author lrosenberg
 * @since 02.04.13 09:12
 */
public class ComponentBean {
	/**
	 * Name of the component.
	 */
	private String name;
	/**
	 * Color of the component.
	 */
	private String color;

	/**
	 * Messages of the updater.
	 */
	private List<String> messages;

	/**
	 * Timestamp of the last update.
	 */
	private String updateTimestamp;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override public String toString(){
		return name+" "+color;
	}

	public void setUpdateTimestamp(String updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public String getUpdateTimestamp() {
		return updateTimestamp;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public int getMessageCount(){
		return messages == null ? 0 : messages.size();
	}
}
