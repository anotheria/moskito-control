package org.anotheria.moskito.control.ui.bean;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 01.04.13 23:07
 */
public class ApplicationBean {
	private String name;
	private String color;
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
