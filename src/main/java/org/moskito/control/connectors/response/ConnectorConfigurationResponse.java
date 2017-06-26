package org.moskito.control.connectors.response;

/**
 * Contains general connector configuration.
 *
 * @author strel
 */
public class ConnectorConfigurationResponse extends ConnectorResponse {

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

	@Override
	public String toString() {
		return "ConnectorInfoResponse{" +
				"supportsInfo=" + supportsInfo +
				", supportsThresholds=" + supportsThresholds +
				", supportsAccumulators=" + supportsAccumulators +
				'}';
	}
}
