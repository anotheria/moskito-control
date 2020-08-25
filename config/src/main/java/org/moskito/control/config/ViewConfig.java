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

	@Configure
	@SerializedName("@charts")
	private String[] charts;

	@Configure
	@SerializedName("@chartTags")
	private String[] chartTags;

	@Configure
	@SerializedName("@componentTags")
	private String[] componentTags;

	@Configure
	@SerializedName("@widgets")
	private String[] widgets;

	@Configure
	@SerializedName("@widgetTags")
	private String[] widgetTags;


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

	public String[] getWidgets() {
		return widgets;
	}

	public void setWidgets(String[] widgets) {
		this.widgets = widgets;
	}

	public String[] getWidgetTags() {
		return widgetTags;
	}

	public void setWidgetTags(String[] widgetTags) {
		this.widgetTags = widgetTags;
	}

	@Override
	public String toString() {
		return "ViewConfig{" +
				"name='" + name + '\'' +
				", componentCategories=" + Arrays.toString(componentCategories) +
				", components=" + Arrays.toString(components) +
				", charts=" + Arrays.toString(charts) +
				", chartTags=" + Arrays.toString(chartTags) +
				", componentTags=" + Arrays.toString(componentTags) +
				", widgets=" + Arrays.toString(widgets) +
				", widgetTags=" + Arrays.toString(widgetTags) +
				'}';
	}
}
