package org.moskito.control.connectors;

import org.moskito.control.connectors.response.ConnectorAccumulatorResponse;
import org.moskito.control.connectors.response.ConnectorAccumulatorsNamesResponse;
import org.moskito.control.connectors.response.ConnectorStatusResponse;
import org.moskito.control.connectors.response.ConnectorThresholdsResponse;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.status.Status;

import java.util.List;
import java.util.Map;

/**
 * This connector class has no real workload, it is used for general testing of the connector infrastructure.
 *
 * @author lrosenberg
 * @since 28.05.13 21:53
 */
public class NoopConnector extends AbstractConnector {

    @Override
	public void configure(String location, String credentials) {
		//DO NOTHING.
	}

	@Override
	public ConnectorStatusResponse getNewStatus() {
		return new ConnectorStatusResponse(new Status(HealthColor.GREEN, "NoCheckByNoop"));
	}

    @Override
    public ConnectorThresholdsResponse getThresholds() {
        return new ConnectorThresholdsResponse();
    }

    @Override
	public ConnectorAccumulatorResponse getAccumulators(List<String> accumulatorNames) {
		return new ConnectorAccumulatorResponse();
	}

    @Override
    public ConnectorAccumulatorsNamesResponse getAccumulatorsNames() {
        return new ConnectorAccumulatorsNamesResponse();
    }

    @Override
    public Map<String, String> getInfo() {
        return null;
    }

}
