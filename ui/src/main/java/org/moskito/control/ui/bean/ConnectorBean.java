package org.moskito.control.ui.bean;

import org.moskito.control.config.ConnectorType;
import org.moskito.control.ui.resource.ControlReplyObject;

import java.util.Map;

/**
 * Bean represents a single configured connector.
 *
 * @author strel
 */
public class ConnectorBean extends ControlReplyObject {

	/**
	 * {@link ConnectorType}.
	 */
	private ConnectorType type;

	/**
	 * Connector general information to show in component inspection.
	 */
	private Map<String, String> info;

	/**
	 * Whether connector may have information message.
	 */
	private boolean supportsInfo;

	/**
	 * Indicates whether connector supports thresholds.
	 */
	private boolean supportsThresholds;

	/**
	 * Indicates whether connector supports accumulators.
	 */
	private boolean supportsAccumulators;


	public ConnectorType getType() {
		return type;
	}

	public void setType(ConnectorType type) {
		this.type = type;
	}

	public Map<String, String> getInfo() {
		return info;
	}

	public void setInfo(Map<String, String> info) {
		this.info = info;
	}

	public boolean isSupportsInfo() {
		return supportsInfo;
	}

	public void setSupportsInfo(boolean supportsInfo) {
		this.supportsInfo = supportsInfo;
	}

	public boolean isSupportsThresholds() {
		return supportsThresholds;
	}

	public void setSupportsThresholds(boolean supportsThresholds) {
		this.supportsThresholds = supportsThresholds;
	}

	public boolean isSupportsAccumulators() {
		return supportsAccumulators;
	}

	public void setSupportsAccumulators(boolean supportsAccumulators) {
		this.supportsAccumulators = supportsAccumulators;
	}
}
