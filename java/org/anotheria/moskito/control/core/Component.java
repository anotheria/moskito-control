package org.anotheria.moskito.control.core;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 26.02.13 01:33
 */
public class Component implements Cloneable{
	private String name;

	private String category;

	private Status status;


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
	}

	@Override
	protected Component clone() {
		try {
			return (Component)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError("can't happen");
		}
	}
}
