package org.moskito.control.core;

import net.anotheria.util.NumberUtils;

/**
 * Contains a single accumulator data entity.
 *
 * @author lrosenberg
 * @since 14.07.13 00:56
 */
public class AccumulatorDataItem {
	/**
	 * Timestamp of the data. Millis since 1970.
	 */
	private long timestamp;
	/**
	 * Value of the data as string.
	 */
	private String value;
	/**
	 * Caption. Caption is build from timestamp in a human readable form.
	 */
	private String caption;

	public AccumulatorDataItem(long aTimestamp, String aValue){
		value = aValue;
		setTimestamp(aTimestamp);
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
		caption = NumberUtils.makeTimeString(timestamp);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override public String toString(){
		return "T: "+getTimestamp()+", C: "+getCaption()+", V: "+getValue();
	}
	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getJson(){
		return "[\""+getCaption()+"\", "+getValue()+"]";
	}

}

