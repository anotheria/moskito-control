package org.moskito.control.config;

import org.configureme.annotations.ConfigureMe;

/**
 * Configuration object for a chart line - a line in a chart.
 *
 * @author lrosenberg
 * @since 18.06.13 13:53
 */
@ConfigureMe
public class ChartLineConfig {
	/**
	 * Name of the component.
	 */
	private String component;
	/**
	 * Name of the accumulator.
	 */
	private String accumulator;

	/**
	 * Caption for the chart line.
	 */
	private String caption;

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getAccumulator() {
		return accumulator;
	}

	public void setAccumulator(String accumulator) {
		this.accumulator = accumulator;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
}
