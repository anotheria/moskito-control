package org.moskito.control.ui.bean;

/**
 * Represents an application in the view.
 *
 * @author lrosenberg
 * @since 01.04.13 23:07
 */
public class ApplicationBean {
	/**
	 * Name of the application.
	 */
	private String name;
	/**
	 * Health status of the application.
	 */
	private String color;
	/**
	 * If true the application is currently selected.
	 */
	private boolean active;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

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
}
