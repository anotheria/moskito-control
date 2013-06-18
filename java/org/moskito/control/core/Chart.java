package org.moskito.control.core;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 18.06.13 23:01
 */
public class Chart {

	private Application parent;

	private String name;

	public Chart(Application aParent, String aName){
		name = aName;
		parent = aParent;
	}

	public Application getParent() {
		return parent;
	}

	public void setParent(Application parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

