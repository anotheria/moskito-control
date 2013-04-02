package org.anotheria.moskito.control.ui.bean;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 02.04.13 09:12
 */
public class ComponentBean {
	private String name;
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
