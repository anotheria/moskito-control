package org.moskito.control.ui.bean;

import net.anotheria.util.NumberUtils;
import org.moskito.control.core.HealthColor;
import org.moskito.control.data.thresholds.DataThreshold;

/**
 * Representation of data threshold item for frontend.
 */
public class DataThresholdBean {
	/**
	 * Name of the threshold
	 */
	private String name;

	/**
	 * Name of the guarded variable.
	 */
	private String guardedVariableName;

	/**
	 * Optional name.
	 */
	private String targetVariableName;

	/**
	 * Curent threshold value.
	 */
	private String value;

	/**
	 * Value before the change.
	 */
	private String previousValue;

	/**
	 * Current color.
	 */
	private String statusColor;

	/**
	 * Previous color.
	 */
	private String previousStatusColor;

	/**
	 * Timestamp of the last change.
	 */
	private String timestamp;

	/**
	 * Counts the number of flips (status changes) by this thresholds.
	 */
	private int flipCount;

	public DataThresholdBean(DataThreshold threshold) {
		this.name = threshold.getName();
		this.guardedVariableName = threshold.getConfig().getGuardedVariableName();
		this.targetVariableName = threshold.getConfig().getTargetVariableName();
		this.value = threshold.getValue();
		this.previousStatusColor = threshold.getPreviousValue();

		HealthColor color = HealthColor.getHealthColor(threshold.getStatus());
		color = (color != null) ? color : HealthColor.NONE;
		this.statusColor = color.name().toLowerCase();

		color = HealthColor.getHealthColor(threshold.getPreviousStatus());
		color = (color != null) ? color : HealthColor.NONE;
		this.previousStatusColor = color.name().toLowerCase();

		this.timestamp = NumberUtils.makeISO8601TimestampString(threshold.getTimestamp());
		this.flipCount = threshold.getFlipCount();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGuardedVariableName() {
		return guardedVariableName;
	}

	public void setGuardedVariableName(String guardedVariableName) {
		this.guardedVariableName = guardedVariableName;
	}

	public String getTargetVariableName() {
		return targetVariableName;
	}

	public void setTargetVariableName(String targetVariableName) {
		this.targetVariableName = targetVariableName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPreviousValue() {
		return previousValue;
	}

	public void setPreviousValue(String previousValue) {
		this.previousValue = previousValue;
	}

	public String getStatusColor() {
		return statusColor;
	}

	public void setStatusColor(String statusColor) {
		this.statusColor = statusColor;
	}

	public String getPreviousStatusColor() {
		return previousStatusColor;
	}

	public void setPreviousStatusColor(String previousStatusColor) {
		this.previousStatusColor = previousStatusColor;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public int getFlipCount() {
		return flipCount;
	}

	public void setFlipCount(int flipCount) {
		this.flipCount = flipCount;
	}
}
