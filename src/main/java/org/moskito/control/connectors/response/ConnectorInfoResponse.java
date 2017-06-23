package org.moskito.control.connectors.response;

import java.util.Map;

/**
 * Contains general connector info.
 *
 * @author strel
 */
public class ConnectorInfoResponse extends ConnectorResponse{

	/**
	 * Connector information message.
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

	/**
	 * Whether connector may have information message.
	 */
	private Map<String, String> info;


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

	public Map<String, String> getInfo() {
		return info;
	}

	public void setInfo(Map<String, String> info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return "ConnectorInfoResponse{" +
				"supportsInfo=" + supportsInfo +
				", supportsThresholds=" + supportsThresholds +
				", supportsAccumulators=" + supportsAccumulators +
				", info='" + info + '\'' +
				'}';
	}
}
