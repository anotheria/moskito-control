package org.moskito.control.ui.bean;

import java.util.ArrayList;
import java.util.List;

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

	/**
	 * Points for this charts.
	 */
	private List<ChartPointBean> points;

	/**
	 * Names of the lines.
	 */
	private List<String> lineNames = new ArrayList<String>();

	/**
	 * Contains last value as additional output possibility.

	 */
	private String lastValue;

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

	public List<ChartPointBean> getPoints() {
		return points;
	}

	public void setPoints(List<ChartPointBean> points) {
		this.points = points;
	}
	public List<String> getLineNames() {
		return lineNames;
	}

	public void setLineNames(List<String> lineNames) {
		this.lineNames = lineNames;
	}

	public void addLineName(String aName){
		lineNames.add(aName);
	}

	public String getLastValue() {
		return lastValue;
	}

	public void setLastValue(String lastValue) {
		this.lastValue = lastValue;
	}


}
