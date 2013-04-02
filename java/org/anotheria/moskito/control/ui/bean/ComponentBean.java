package org.anotheria.moskito.control.ui.bean;

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
}
