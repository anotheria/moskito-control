package org.moskito.control.ui.bean;

import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 02.04.13 11:46
 */
public class ComponentHolderBean {
	private List<ComponentBean> components;
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
