package org.moskito.control.core;

import org.moskito.control.core.status.Status;
import org.moskito.control.core.status.StatusChangeEvent;

/**
 * Represents a component in an application.
 *
 * @author lrosenberg
 * @since 26.02.13 01:33
 */
public class Component implements Cloneable{
	/**
	 * Name of the component.
	 */
	private String name;

	/**
	 * Category of the component.
	 */
	private String category;

	/**
	 * Current status of the category.
	 */
	private Status status;

	/**
	 * Timestamp of the last update.
	 */
	private long lastUpdateTimestamp;

	/**
	 * Link to the parent application.
	 */
	private Application parent;

    /**
	 * Creates a new component.
	 * @param aParent the parent application.
	 */
	public Component(Application aParent){
		parent = aParent;
		status = new Status(HealthColor.NONE, "None yet");
	}

	public HealthColor getHealthColor() {
		return status.getHealth();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		lastUpdateTimestamp = System.currentTimeMillis();
		Status oldStatus = this.status;
		this.status = status;
		if(oldStatus.getHealth() != status.getHealth()){
            StatusChangeEvent event = new StatusChangeEvent(parent, this, oldStatus, status, lastUpdateTimestamp);
			ApplicationRepository.getInstance().addStatusChange(event);
		}
	}

	public long getLastUpdateTimestamp(){
		return lastUpdateTimestamp;
	}

	@Override
	protected Component clone() {
		try {
			return (Component)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError("can't happen");
		}
	}

	@Override public String toString(){
		return name;
	}

}
