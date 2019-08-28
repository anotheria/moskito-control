package org.moskito.control.config;

import com.google.gson.annotations.SerializedName;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

import java.util.Arrays;

/**
 * Represents a view configuration. Since 2.x.
 *
 * @author lrosenberg
 */
@ConfigureMe
public class ViewConfig {
	/**
	 * Name of the view.
	 */
	@Configure
	@SerializedName("name")
	private String name;
	@Configure
	@SerializedName("@componentCategories")
	private String[] componentCategories;
	@Configure
	@SerializedName("@components")
	private String[] components;

	@SerializedName("@charts")
	private String[] charts;

	@SerializedName("@chartTags")
	private String[] chartTags;

	@SerializedName("@componentTags")
	private String[] componentTags;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getComponentCategories() {
		return componentCategories;
	}

	public void setComponentCategories(String[] componentCategories) {
		this.componentCategories = componentCategories;
	}

	public String[] getComponents() {
		return components;
	}

	public void setComponents(String[] components) {
		this.components = components;
	}

	public String[] getCharts() {
		return charts;
	}

	public void setCharts(String[] charts) {
		this.charts = charts;
	}

	public String[] getChartTags() {
		return chartTags;
	}

	public void setChartTags(String[] chartTags) {
		this.chartTags = chartTags;
	}

	public String[] getComponentTags() {
		return componentTags;
	}

	public void setComponentTags(String[] componentTags) {
		this.componentTags = componentTags;
	}

	@Override
	public String toString() {
		return "ViewConfig{" +
				"name='" + name + '\'' +
				", componentCategories=" + Arrays.toString(componentCategories) +
				", components=" + Arrays.toString(components) +
				'}';
	}
}
