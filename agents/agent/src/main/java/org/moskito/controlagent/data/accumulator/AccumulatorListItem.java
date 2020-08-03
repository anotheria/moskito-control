package org.moskito.controlagent.data.accumulator;

import java.io.Serializable;

/**
 * List item for the accumulator list.
 *
 * @author lrosenberg
 * @since 18.06.13 08:45
 */
public class AccumulatorListItem implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * Name of the accumulator.
	 */
	private String name;
	/**
	 * Number of collected items.
	 */
	private int values;

	public AccumulatorListItem(String aName, int aValuesCount){
		name = aName;
		values = aValuesCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValues() {
		return values;
	}

	public void setValues(int values) {
		this.values = values;
	}

	@Override public String toString(){
		return name+", "+values;
	}
}
