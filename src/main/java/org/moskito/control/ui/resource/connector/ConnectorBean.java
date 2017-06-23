package org.moskito.control.ui.resource.connector;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * Bean represents a single configured connector.
 *
 * @author strel
 */
@XmlRootElement
public class ConnectorBean {

	/**
	 * Connector general information to show in component inspection.
	 */
	@XmlElement
	private Map<String, String> info;

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
