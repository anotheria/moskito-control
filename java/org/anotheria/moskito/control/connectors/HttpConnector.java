package org.anotheria.moskito.control.connectors;

import org.anotheria.moskito.control.core.HealthColor;
import org.anotheria.moskito.control.core.Status;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 28.05.13 21:01
 */
public class HttpConnector implements Connector {
	private String location;

	@Override
	public void configure(String location) {
		this.location = location;
	}

	@Override
	public ConnectorResponse getNewStatus() {
		System.out.println("Trying to call "+location);
		return new ConnectorResponse(new Status(HealthColor.GREEN, "NoCheckByHttpYet"));
	}

}
