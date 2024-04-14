package org.moskito.control.ui.restapi.control;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * This is a container bean for threshold names.
 */
@Schema(name = "ThresholdBean", description = "A single Threshold state.")
public class ThresholdBean  {

	/**
	 * Threshold name.
	 */
	@Schema(description = "Name of the threshold.")
	private String name;

	/**
	 * String representation of threshold status.
	 */
	@Schema(description = "Current status as string")
	private String status;

	/**
	 * Last threshold value.
	 */

	@Schema(description = "Last value that triggered the threshold.")
	private String lastValue;

	/**
	 * String representation of last threshold update timestamp.
	 */
	@Schema(description = "Last change timestamp as string.")
	private String statusChangeTimestamp;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLastValue() {
		return lastValue;
	}

	public void setLastValue(String lastValue) {
		this.lastValue = lastValue;
	}

	public String getStatusChangeTimestamp() {
		return statusChangeTimestamp;
	}

	public void setStatusChangeTimestamp(String statusChangeTimestamp) {
		this.statusChangeTimestamp = statusChangeTimestamp;
	}
}
