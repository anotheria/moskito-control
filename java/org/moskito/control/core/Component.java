package org.moskito.control.core;

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

	private long lastUpdateTimestamp;


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
		this.status = status;
		lastUpdateTimestamp = System.currentTimeMillis();
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
