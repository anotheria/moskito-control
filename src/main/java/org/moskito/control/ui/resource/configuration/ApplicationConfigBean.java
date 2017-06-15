package org.moskito.control.ui.resource.configuration;

import org.moskito.control.config.ChartConfig;
import org.moskito.control.config.ComponentConfig;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Configuration of the application.
 */
@XmlRootElement
public class ApplicationConfigBean {
	/**
	 * Name of the application.
	 */
	@XmlElement private String name;

	/**
	 * Components.
	 */
	@XmlElement private ComponentConfig[] components;

	/**
	 * Charts.
	 */
	@XmlElement
	private ChartConfig[] charts;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ComponentConfig[] getComponents() {
		return components;
	}

	public void setComponents(ComponentConfig[] components) {
		this.components = components;
	}

	public ChartConfig[] getCharts() {
		return charts;
	}

	public void setCharts(ChartConfig[] charts) {
		this.charts = charts;
	}
}
