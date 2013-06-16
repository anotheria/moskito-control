package org.moskito.control.connectors;

import org.moskito.control.core.HealthColor;
import org.moskito.control.core.Status;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 28.05.13 21:53
 */
public class NoopConnector implements Connector {
	@Override
	public void configure(String location) {
		//DO NOTHING.
	}

	@Override
	public ConnectorResponse getNewStatus() {
		return new ConnectorResponse(new Status(HealthColor.GREEN, "NoCheckByNoop"));
	}
}