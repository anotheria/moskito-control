package org.moskito.control.data.thresholds;

import net.anotheria.moskito.core.threshold.ThresholdStatus;
import net.anotheria.util.NumberUtils;

/**
 * Created whenever a barrier in threshold is broken and a threshold gets new status.
 */
public class DataThresholdAlert {

	/**
	 * Timestamp of change.
	 */
	private long timestamp;

	/**
	 * Corresponding threshold.
	 */
	private DataThreshold threshold;
	
	/**
	 * Status before the change.
	 */
	private ThresholdStatus previousStatus;

	/**
	 * Status after the change.
	 */
	private ThresholdStatus status;

	/**
	 * Value before the change.
	 */
	private String previousValue;
	
	/**
	 * Value after the change.
	 */
	private String value;


	public DataThresholdAlert(DataThreshold threshold) {
		this.timestamp = System.currentTimeMillis();
		this.threshold = threshold;
		this.previousStatus = threshold.getPreviousStatus();
		this.status = threshold.getStatus();
		this.previousValue = threshold.getPreviousValue();
		this.value = threshold.getValue();
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public DataThreshold getThreshold() {
		return threshold;
	}

	public void setThreshold(DataThreshold threshold) {
		this.threshold = threshold;
	}

	public ThresholdStatus getPreviousStatus() {
		return previousStatus;
	}

	public void setPreviousStatus(ThresholdStatus previousStatus) {
		this.previousStatus = previousStatus;
	}

	public ThresholdStatus getStatus() {
		return status;
	}

	public void setStatus(ThresholdStatus status) {
		this.status = status;
	}

	public String getPreviousValue() {
		return previousValue;
	}

	public void setPreviousValue(String previousValue) {
		this.previousValue = previousValue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return NumberUtils.makeISO8601TimestampString(getTimestamp()) + ' '
				+ threshold.getName() + ", (" + getPreviousStatus() + "/ " + getPreviousValue() + ") --> " +
				'(' + getStatus() + "/ " + getValue() + ')';
	}
}
