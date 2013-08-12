package org.moskito.control.ui.bean;

import java.util.List;

/**
 * Container for components in a category.
 *
 * @author lrosenberg
 * @since 02.04.13 11:46
 */
public class ComponentHolderBean {
	/**
	 * Components.
	 */
	private List<ComponentBean> components;
	/**
	 * Category.
	 */
	private CategoryBean category;

	public List<ComponentBean> getComponents() {
		return components;
	}

	public void setComponents(List<ComponentBean> components) {
		this.components = components;
	}

	public CategoryBean getCategory() {
		return category;
	}

	public void setCategory(CategoryBean category) {
		this.category = category;
	}

	public String getCategoryName(){
		return getCategory().getName();
	}

	public String getHealth(){
		return getCategory().getHealth();
	}
}
