package org.moskito.control.core;

import net.anotheria.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * TODO comment this class
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
	 * Data.
	 */
	private List<AccumulatorDataItem> data = new ArrayList<AccumulatorDataItem>();

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

	public List<AccumulatorDataItem> getData() {
		return data;
	}

	public void setData(List<AccumulatorDataItem> data) {
		this.data = data;
	}

	/**
	 * This is a helper map that contains characters an accumulator name can contains, that are prohibited in js variables and therefore have to be mapped.
	 */
	private static final HashMap<String, String> jsReplaceMap;
	static{
		jsReplaceMap = new HashMap<String, String>();
		jsReplaceMap.put(" ", "_");
		jsReplaceMap.put("-", "_");
		jsReplaceMap.put("+", "_");
		jsReplaceMap.put(".", "_");
	}

	public String getJsName(){
		String jsVariableName = getComponent()+"_"+getAccumulator();
		return StringUtils.replace(jsVariableName, jsReplaceMap);
	}
}

