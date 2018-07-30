package org.moskito.control.ui.bean;

import net.anotheria.util.NumberUtils;
import org.moskito.control.core.HealthColor;
import org.moskito.control.data.thresholds.DataThresholdAlert;

/**
 * Representation of data threshold alert history item for frontend.
 */
public class DataThresholdAlertBean {

	/**
	 * Threshold name.
	 */
	private String name;

	/**
	 * Timestamp of change.
	 */
	private String timestamp;

	/**
	 * Color (status) code before the change.
	 */
	private String previousStatusColor;
	/**
	 * Color (status) after the change.
	 */
	private String statusColor;

	/**
	 * Value before the change.
	 */
	private String previousValue;
	
	/**
	 * Value after the change.
	 */
	private String value;

	public DataThresholdAlertBean(DataThresholdAlert alert) {
		this.name = alert.getThreshold().getName();
		this.timestamp = NumberUtils.makeISO8601TimestampString(alert.getTimestamp());
		this.previousValue = alert.getPreviousValue();
		this.value = alert.getValue();

		HealthColor color = HealthColor.getHealthColor(alert.getStatus());
		color = (color != null) ? color : HealthColor.NONE;
		this.statusColor = color.name().toLowerCase();

		color = HealthColor.getHealthColor(alert.getPreviousStatus());
		color = (color != null) ? color : HealthColor.NONE;
		this.previousStatusColor = color.name().toLowerCase();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getPreviousStatusColor() {
		return previousStatusColor;
	}

	public void setPreviousStatusColor(String previousStatusColor) {
		this.previousStatusColor = previousStatusColor;
	}

	public String getStatusColor() {
		return statusColor;
	}

	public void setStatusColor(String statusColor) {
		this.statusColor = statusColor;
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
}
