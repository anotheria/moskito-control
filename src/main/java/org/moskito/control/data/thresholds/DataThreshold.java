package org.moskito.control.data.thresholds;

import net.anotheria.moskito.core.threshold.ThresholdStatus;
import org.moskito.control.core.Application;
import org.moskito.control.core.Component;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.history.StatusUpdateHistoryRepository;
import org.moskito.control.core.status.Status;
import org.moskito.control.core.status.StatusChangeEvent;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * This class represents a data threshold object.
 */
public class DataThreshold {
	/**
	 * Name of the threshold
	 */
	private String name;

	/**
	 * Status of the threshold.
	 */
	private ThresholdStatus status = ThresholdStatus.OFF;

	/**
	 * Previous status.
	 */
	private ThresholdStatus previousStatus = ThresholdStatus.OFF;

	/**
	 * Curent threshold value.
	 */
	private String value = "none yet";

	/**
	 * Value before the change.
	 */
	private String previousValue = "none yet";

	/**
	 * Timestamp of the last value change.
	 */
	private long timestamp;

	/**
	 * Config data from config file.
	 */
	private DataThresholdConfig config = new DataThresholdConfig();

	/**
	 * Counts the number of flips (status changes) by this thresholds.
	 */
	private int flipCount = 0;

	public DataThreshold() {
		timestamp = System.currentTimeMillis();
	}

	@Override
	public String toString() {
		return getName() + ", " + getPreviousStatus() + " -> " + getStatus() + ", previousvalue: " + getPreviousValue() + ", value: " + getValue() + ", " + getConfig();
	}

	public void configure(String guardedVariableName, List<String> thresholdConfigs) {
		config.configure(guardedVariableName, thresholdConfigs);
		name = config.getTargetVariableName();
	}

	public void update(String newValue) {
		ThresholdStatus futureStatus = getNewStatus(newValue);

		previousValue = value;
		value = newValue;
		timestamp = System.currentTimeMillis();

		if (status != futureStatus) {
			previousStatus = status;
			status = futureStatus;
			flipCount++;

			Application app = new Application("Thresholds");
			StatusChangeEvent event = new StatusChangeEvent();
			event.setApplication(app);
			event.setStatus(new Status(HealthColor.getHealthColor(status), value));
			event.setOldStatus(new Status(HealthColor.getHealthColor(previousStatus), previousValue));
			event.setTimestamp(timestamp);
			event.setComponent(new Component(app, name));
			StatusUpdateHistoryRepository.getInstance().notifyStatusChange(event);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ThresholdStatus getStatus() {
		return status;
	}

	public void setStatus(ThresholdStatus status) {
		this.status = status;
	}

	public ThresholdStatus getPreviousStatus() {
		return previousStatus;
	}

	public void setPreviousStatus(ThresholdStatus previousStatus) {
		this.previousStatus = previousStatus;
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

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public DataThresholdConfig getConfig() {
		return config;
	}

	public void setConfig(DataThresholdConfig config) {
		this.config = config;
	}

	public int getFlipCount() {
		return flipCount;
	}

	public void setFlipCount(int flipCount) {
		this.flipCount = flipCount;
	}

	public ThresholdStatus getNewStatus(String newValue) {
		ThresholdStatus newStatus = ThresholdStatus.GREEN;
		Double value = null;

		try {
			value = Double.parseDouble(newValue);
		} catch (NumberFormatException e) {
			LoggerFactory.getLogger(DataThreshold.class).error(newValue + " is not a number");
			return newStatus;
		}

		Double guardValue;
		int directionSign;

		for (GuardConfig guard : config.getGuards()) {
			guardValue = Double.parseDouble(guard.getValue());
			directionSign = (guard.getDirection() == GuardDirection.UP) ? 1 : -1;

			if (directionSign * value.compareTo(guardValue) > 0) {
				newStatus = convertHealthColor(guard.getColor());
			}
		}

		return newStatus;
	}

	private ThresholdStatus convertHealthColor(HealthColor color) {
		switch (color) {
			case NONE:
				return ThresholdStatus.OFF;
			case GREEN:
				return ThresholdStatus.GREEN;
			case YELLOW:
				return ThresholdStatus.YELLOW;
			case ORANGE:
				return ThresholdStatus.ORANGE;
			case RED:
				return ThresholdStatus.RED;
			case PURPLE:
				return ThresholdStatus.PURPLE;
			default:
				return null;
		}
	}
}
