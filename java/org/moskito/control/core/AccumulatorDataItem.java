package org.moskito.control.core;

import net.anotheria.util.NumberUtils;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 14.07.13 00:56
 */
public class AccumulatorDataItem {
	private long timestamp;
	private String value;
	private String caption;

	public AccumulatorDataItem(long aTimestamp, String aValue){
		timestamp = aTimestamp;
		value = aValue;
		caption = NumberUtils.makeTimeString(timestamp);
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
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

