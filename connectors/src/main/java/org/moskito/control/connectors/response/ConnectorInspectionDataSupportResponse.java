package org.moskito.control.connectors.response;

/**
 * Contains information about supported inspection data.
 *
 * @author strel
 */
public class ConnectorInspectionDataSupportResponse {

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

	/**
	 * Indicates whether connector supports component's config.
	 */
	private boolean supportsConfig;

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

	public boolean isSupportsConfig() {
		return supportsConfig;
	}

	public void setSupportsConfig(boolean supportsConfig) {
		this.supportsConfig = supportsConfig;
	}

	@Override
	public String toString() {
		return "ConnectorInspectionDataSupportResponse{" +
				"supportsInfo=" + supportsInfo +
				", supportsThresholds=" + supportsThresholds +
				", supportsAccumulators=" + supportsAccumulators +
				", supportsConfig=" + supportsConfig +
				'}';
	}
}
