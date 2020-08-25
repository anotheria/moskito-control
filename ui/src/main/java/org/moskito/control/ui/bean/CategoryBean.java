package org.moskito.control.ui.bean;

import org.moskito.control.common.HealthColor;

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

	/**
	 * If true this category is actually selected.
	 */
	private boolean selected;

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

	public boolean isAll() {
		return all;
	}

	public void setAll(boolean all) {
		this.all = all;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CategoryBean)) return false;

		CategoryBean that = (CategoryBean) o;

		if (name != null ? !name.equals(that.name) : that.name != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}
}
