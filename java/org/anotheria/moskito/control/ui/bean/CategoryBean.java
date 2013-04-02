package org.anotheria.moskito.control.ui.bean;

import org.anotheria.moskito.control.core.HealthColor;

/**
 * Represents a single category in the view menu.
 *
 * @author lrosenberg
 * @since 02.04.13 00:32
 */
public class CategoryBean implements Comparable<CategoryBean>{
	/**
	 * Name of the category.
	 */
	private String name;
	/**
	 * Worst health status.
	 */
	private HealthColor worstStatus;
	/**
	 * Number of components in this category.
	 */
	private int componentCount;
	/**
	 * Is this the 'all' category. This is a stupid hack, I know.
	 */
	private boolean all;

	public boolean isAll() {
		return all;
	}

	public void setAll(boolean all) {
		this.all = all;
	}

	public CategoryBean(String aName){
		name = aName;
		worstStatus = HealthColor.GREEN;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HealthColor getWorstStatus() {
		return worstStatus;
	}

	public void setWorstStatus(HealthColor worstStatus) {
		this.worstStatus = worstStatus;
	}

	public int getComponentCount() {
		return componentCount;
	}

	public void setComponentCount(int componentCount) {
		this.componentCount = componentCount;
	}

	public void processStatus(HealthColor anotherColor){
		if (anotherColor.ordinal()>worstStatus.ordinal())
			worstStatus = anotherColor;
		componentCount++;
	}

	@Override
	public int compareTo(CategoryBean o) {
		return name.compareTo(o.getName());
	}

	public String getHealth(){
		return worstStatus.toString().toLowerCase();
	}
}
