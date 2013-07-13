package org.moskito.control.core;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 14.07.13 00:56
 */
public class AccumulatorDataItem {
	private long timestamp;
	private String value;

	public AccumulatorDataItem(){

	}

	public AccumulatorDataItem(long aTimestamp, String aValue){
		timestamp = aTimestamp;
		value = aValue;
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
		return "T: "+getTimestamp()+", V: "+getValue();
	}
}

