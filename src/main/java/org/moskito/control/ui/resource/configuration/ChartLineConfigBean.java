package org.moskito.control.ui.resource.configuration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Configuration object for a chart line - a line in a chart.
 */
@XmlRootElement
public class ChartLineConfigBean {
	/**
	 * Name of the component.
	 */
	@XmlElement
	private String component;
	/**
	 * Name of the accumulator.
	 */
	@XmlElement
	private String accumulator;


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
}
