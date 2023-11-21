package org.moskito.control.config;

import com.google.gson.annotations.SerializedName;
import org.configureme.annotations.ConfigureMe;

/**
 * Configuration object for a chart line - a line in a chart.
 *
 * @author lrosenberg
 * @since 18.06.13 13:53
 */
@ConfigureMe(allfields = true)
public class ChartLineConfig {
	/**
	 * Name of the component.
	 */
	@SerializedName("component")
	private String component;
	/**
	 * Name of the accumulator.
	 */
	@SerializedName("accumulator")
	private String accumulator;
	
	/**
	 * Caption for the chart line.
	 */
	@SerializedName("caption")
	private String caption;

	@SerializedName("componentTags")
	private String componentTags;

	public void setComponent(String component) {
		this.component = component;
	}

	public String getComponent() {
		return component;
	}

	public String getAccumulator() {
		return accumulator;
	}

	public void setAccumulator(String accumulator) {
		this.accumulator = accumulator;
	}

	public String getComponentTags() {
		return componentTags;
	}

	public void setComponentTags(String componentTags) {
		this.componentTags = componentTags;
	}

	/**
	 * Method used to get caption in case this config creates
	 * multiple charts.
	 * If caption present in config, returns caption string
	 * with appended component name in round bracers.
	 * Else return null
	 *
	 * @param componentName name of component to get caption
	 *
	 * @return caption string
	 */
	public String getCaption(String componentName) {
		return getCaption() != null ?  getCaption() + " (" + componentName + ")" : null;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	

	@Override
	public String toString() {
		return "ChartLineConfig{" +
				"component='" + component + '\'' +
				", accumulator='" + accumulator + '\'' +
				", caption='" + caption + '\'' +
				", componentTags='" + componentTags + '\'' +
				'}';
	}
}
