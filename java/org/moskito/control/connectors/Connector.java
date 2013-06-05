package org.moskito.control.connectors;

/**
 * Connector
 *
 * @author lrosenberg
 * @since 26.02.13 18:44
 */
public interface Connector {
	void configure(String location);

	ConnectorResponse getNewStatus();
}
