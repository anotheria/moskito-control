package org.moskito.control.core;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 21.06.13 13:53
 */
public class ChartLine {
	private String component;
	private String accumulator;

	public ChartLine(String aComponent, String anAccumulator){
		component = aComponent;
		accumulator = anAccumulator;
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
}
