package org.moskito.control.ui.restapi.control;

import org.moskito.control.ui.resource.ControlReplyObject;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * This is a container bean for threshold names.
 */
@XmlRootElement
public class ThresholdBean  {

	/**
	 * Threshold name.
	 */
	@XmlElement
	private String name;

	/**
	 * String representation of threshold status.
	 */
	@XmlElement
	private String status;

	/**
	 * Last threshold value.
	 */
	@XmlElement
	private String lastValue;

	/**
	 * String representation of last threshold update timestamp.
	 */
	@XmlElement
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
