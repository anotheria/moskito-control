package org.moskito.control.connectors;

import java.util.List;

/**
 * Connector.
 *
 * @author lrosenberg
 * @since 26.02.13 18:44
 */
public interface Connector {
	/**
	 * Called after initialization of the connector in order to provider target's location.
	 * @param location
	 */
	void configure(String location);

	/**
	 * Returns the status of the application.
	 * @return
	 */
	ConnectorStatusResponse getNewStatus();

	/**
	 * Returns the accumulator data.
	 * @param accumulatorNames names of accumulators to retrieve.
	 * @return
	 */
	ConnectorAccumulatorResponse getAccumulators(List<String> accumulatorNames);
}
