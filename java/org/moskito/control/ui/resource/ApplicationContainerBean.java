package org.moskito.control.ui.resource;

import org.moskito.control.core.HealthColor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * This is helper object for applications.
 *
 * @author lrosenberg
 * @since 05.06.13 22:24
 */
@XmlRootElement
public class ApplicationContainerBean {
	/**
	 * Name of the component.
	 */
	@XmlElement
	private String name;

	/**
	 * Current application color - overall application state.
	 */
	@XmlElement
	private HealthColor applicationColor;

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

	public HealthColor getApplicationColor() {
		return applicationColor;
	}

	public void setApplicationColor(HealthColor applicationColor) {
		this.applicationColor = applicationColor;
	}


	public void addComponent(ComponentBean cBean) {
		components.add(cBean);
	}
}
