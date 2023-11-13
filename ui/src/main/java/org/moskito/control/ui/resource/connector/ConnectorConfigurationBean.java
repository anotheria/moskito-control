package org.moskito.control.ui.resource.connector;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Bean represents a single configured connector.
 *
 * @author strel
 */
@XmlRootElement
public class ConnectorConfigurationBean {

	/**
	 * Whether connector may have information message.
	 */
	@XmlElement
	private boolean supportsInfo;

	/**
	 * Indicates whether connector supports thresholds.
	 */
	@XmlElement
	private boolean supportsThresholds;

	/**
	 * Indicates whether connector supports accumulators.
	 */
	@XmlElement
	private boolean supportsAccumulators;


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
