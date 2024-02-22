package org.moskito.control.ui.restapi.control;

import org.moskito.control.common.HealthColor;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * This is helper object for applications.
 *
 * @author lrosenberg
 * @since 05.06.13 22:24
 */
@XmlRootElement
public class ViewContainerBean {
	/**
	 * Name of the component.
	 */
	@XmlElement
	private String name;

	/**
	 * Current view color - overall view state.
	 */
	@XmlElement
	private HealthColor viewColor;

	/**
	 * Components that are part of this application.
	 */
	@XmlElement
	private List<ComponentBean> components = new ArrayList<ComponentBean>();

	public List<ComponentBean> getComponents() {
		return components;
	}

	public void setComponents(List<ComponentBean> components) {
		this.components = components;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HealthColor getViewColor() {
		return viewColor;
	}

	public void setViewColor(HealthColor viewColor) {
		this.viewColor = viewColor;
	}


	public void addComponent(ComponentBean cBean) {
		components.add(cBean);
	}
}
