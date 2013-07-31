package org.moskito.control.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a single line in a chart.
 *
 * @author lrosenberg
 * @since 21.06.13 13:53
 */
public class ChartLine {
	/**
	 * Component source for this line.
	 */
	private String component;
	/**
	 * Accumulator name for this line.
	 */
	private String accumulator;

	/**
	 * Caption for the chart line.
	 */
	private String chartCaption;

	/**
	 * Data.
	 */
	private List<AccumulatorDataItem> data = new ArrayList<AccumulatorDataItem>();

	public ChartLine(String aComponent, String anAccumulator, String aChartCaption){
		component = aComponent;
		accumulator = anAccumulator;
		chartCaption = aChartCaption;
	}

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

	public List<AccumulatorDataItem> getData() {
		return data;
	}

	public void setData(List<AccumulatorDataItem> data) {
		this.data = data;
	}

	public String getChartCaption() {
		return (chartCaption != null && chartCaption.length()>0) ? chartCaption :  (getAccumulator()+"@"+getComponent());
	}

	public void setChartCaption(String chartCaption) {
		this.chartCaption = chartCaption;
	}

	@Override public String toString(){
		StringBuilder ret = new StringBuilder();

		ret.append(getAccumulator()).append("@").append(getComponent());
		if (chartCaption!=null && chartCaption.length()>0)
			ret.append(" as ").append(chartCaption).append(" ");
		ret.append("with ").append(data==null?"none":""+data.size()).append(" elements.");
		return ret.toString();
	}
}

