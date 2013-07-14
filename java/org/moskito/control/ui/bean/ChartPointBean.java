package org.moskito.control.ui.bean;

import net.anotheria.util.BasicComparable;
import net.anotheria.util.sorter.IComparable;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 14.07.13 01:50
 */
public class ChartPointBean implements IComparable{
	private String caption;
	private List<String> values = new ArrayList<String>();
	private long timestamp;

	public ChartPointBean(String aCaption, long aTimestamp){
		caption = aCaption;
		timestamp = aTimestamp;
	}

	public void addValue(String value){
		values.add(value);
	}

	public List<String> getValues(){
		return values;
	}

	/**
	 * Adds values to the current point bean to ensure that we have one value for each caption. This is needed to
	 * fill up the holes in graphs, if some graph misses data for some point of time.
	 * @param currentLineCount
	 */
	public void ensureLength(int currentLineCount) {
		while(values.size()<currentLineCount)
			values.add("0");
	}

	@Override public String toString(){
		StringBuilder ret = new StringBuilder("[");
		ret.append("\"").append(caption).append("\"");
		for (String s: values)
			ret.append(",").append(s);
		ret.append("]");
		return ret.toString();
	}

	@Override
	public int compareTo(IComparable iComparable, int i) {
		return BasicComparable.compareLong(timestamp, ((ChartPointBean)iComparable).timestamp);
	}
}
