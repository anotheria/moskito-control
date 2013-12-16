package org.moskito.control.ui.bean;

import net.anotheria.util.BasicComparable;
import net.anotheria.util.NumberUtils;
import net.anotheria.util.sorter.IComparable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * A single point on the x axis.
 *
 * @author lrosenberg
 * @since 14.07.13 01:50
 */
@XmlRootElement
public class ChartPointBean implements IComparable{

	/**
	 * Constant that is used to fill the empty spaces.
	 */
	public static final String TEMPORARLY_FILL_OUT_STRING = "XXX";

	/**
	 * Points caption (timestamp in human readable form).
	 */
	@XmlElement
	private String caption;
	/**
	 * Values for different lines.
	 */
	@XmlElement
	private List<String> values = new ArrayList<String>();
	/**
	 * Timestamp.
	 */
	@XmlElement
	private long timestamp;

	public ChartPointBean(String aCaption, long aTimestamp){
		caption = aCaption;
		timestamp = aTimestamp;
	}

	/**
	 * Adds a new value to this x-axis coordinates.
	 * @param value
	 */
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
			values.add(TEMPORARLY_FILL_OUT_STRING);
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

	public boolean isEmptyValueAt(int v) {
		return values.get(v).equals(TEMPORARLY_FILL_OUT_STRING);
	}

	public void setValueAt(int v, String s) {
		values.set(v, s);
	}

	public String getValueAt(int v){
		return values.get(v);
	}

	public String getCaption(){
		return caption;
	}

	@XmlElement(name="debugTs")
	public String getDebugTimestamp(){
		return NumberUtils.makeISO8601TimestampString(timestamp);
	}

	public long getTimestamp(){
		return timestamp;
	}
}


