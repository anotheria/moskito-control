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
     * Thresholds of the component.
     */
    private List<ThresholdBean> thresholds;

	/**
	 * Messages of the updater.
	 */
	private List<String> messages;

	/**
	 * Timestamp of the last update.
	 */
	private String updateTimestamp;

	/**
	 * Added for new shorter status beta.
	 */
	private String categoryName;


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

    public List<ThresholdBean> getThresholds() {
        return thresholds;
    }

    public void setThresholds(List<ThresholdBean> thresholds) {
        this.thresholds = thresholds;
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

	@Override public String toString(){
		return name+" "+color;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}
