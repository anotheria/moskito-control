package org.moskito.control.ui.bean;

/**
 * Represents a single chart object.
 *
 * @author lrosenberg
 * @since 18.06.13 23:08
 */
public class ChartBean {
	/**
	 * Name of the chart.
	 */
	private String name;
	/**
	 * If of the div that contains the chart.
	 */
	private String divId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDivId() {
		return divId;
	}

	public void setDivId(String divId) {
		this.divId = divId;
	}
}
