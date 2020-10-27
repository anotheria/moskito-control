package org.moskito.control.connectors;

import org.moskito.control.common.AccumulatorDataItem;
import org.moskito.control.common.HealthColor;
import org.moskito.control.config.ComponentConfig;
import org.moskito.control.connectors.response.ConnectorConfigResponse;
import org.moskito.control.connectors.response.ConnectorNowRunningResponse;
import org.moskito.controlagent.data.threshold.ThresholdDataItem;

public abstract class AbstractConnector implements Connector{

    public boolean supportsInfo(){
        return false;
    }

    public boolean supportsThresholds(){
        return false;
    }

    public boolean supportsAccumulators(){
        return false;
    }

	@Override
	public boolean supportsConfig() {
		return false;
	}

	@Override
	public boolean supportsNowRunning() {
		return false;
	}

	@Override
	public ConnectorConfigResponse getConfig() {
		return null;
	}

	/**
	 * Maps agent threshold item to internally used control item.
	 * @param agentItem
	 * @return threshold item
	 */
	protected org.moskito.control.common.ThresholdDataItem agent2control(ThresholdDataItem agentItem){
		org.moskito.control.common.ThresholdDataItem controlItem = new org.moskito.control.common.ThresholdDataItem();
		controlItem.setLastValue(agentItem.getLastValue());
		controlItem.setName(agentItem.getName());
		controlItem.setStatus(HealthColor.getHealthColor(agentItem.getStatus()));
		controlItem.setStatusChangeTimestamp(agentItem.getStatusChangeTimestamp());
		return controlItem;
	}

	protected AccumulatorDataItem agent2control(org.moskito.controlagent.data.accumulator.AccumulatorDataItem agentItem){
		AccumulatorDataItem controlItem = new AccumulatorDataItem(agentItem.getTimestamp(), agentItem.getValue());
		return controlItem;
	}

	@Override
	public ConnectorNowRunningResponse getNowRunning() {
		throw new UnsupportedOperationException("This connector doesn't support getNowRunning, call supportsNowRunning() first");
	}

	@Override
	public void configure(ComponentConfig config) {
		configure(config.getName(), config.getLocation(), config.getCredentials());
	}
}
