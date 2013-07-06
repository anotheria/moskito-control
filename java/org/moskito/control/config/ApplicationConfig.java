package org.moskito.control.config;

import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

/**
 * Configuration of the application.
 *
 * @author lrosenberg
 * @since 26.02.13 01:33
 */
@ConfigureMe
public class ApplicationConfig {
	/**
	 * Name of the application.
	 */
	@Configure private String name;

	/**
	 * Components.
	 */
	@Configure private ComponentConfig[] components;

	/**
	 * Charts.
	 */
	@Configure private ChartConfig[] charts;

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

	public ComponentConfig getComponent(String name){
		for (ComponentConfig c : components){
			if (c.getName().equals(name))
				return c;
		}
		throw new IllegalArgumentException("Component with name "+name+" not found");
	}

	public ChartConfig[] getCharts() {
		return charts;
	}

	public void setCharts(ChartConfig[] charts) {
		this.charts = charts;
	}



}
