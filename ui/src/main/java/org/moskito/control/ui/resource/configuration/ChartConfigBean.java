package org.moskito.control.ui.resource.configuration;

import org.moskito.control.config.ChartLineConfig;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Configuration object for a chart.
 */
@XmlRootElement
public class ChartConfigBean {

	/**
	 * Name of the chart.
	 */
	@XmlElement
	private String name;

	/**
	 * Lines that are part of the chart.
	 */
	@XmlElement
	private ChartLineConfig[] lines;

	/**
	 * Max number of elements that should be used. Exceeding elements will be cut off.
	 */
	@XmlElement
	private int limit;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ChartLineConfig[] getLines() {
		return lines;
	}

	public void setLines(ChartLineConfig[] lines) {
		this.lines = lines;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
