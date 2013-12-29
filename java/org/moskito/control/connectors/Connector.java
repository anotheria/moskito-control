package org.moskito.control.connectors;

import org.moskito.control.connectors.response.ConnectorAccumulatorResponse;
import org.moskito.control.connectors.response.ConnectorAccumulatorsNamesResponse;
import org.moskito.control.connectors.response.ConnectorStatusResponse;
import org.moskito.control.connectors.response.ConnectorThresholdsResponse;

import java.io.IOException;
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
	 * @return {@link ConnectorStatusResponse}
	 */
	ConnectorStatusResponse getNewStatus();

    /**
     * Returns the threshold data.
     * @return {@link ConnectorThresholdsResponse}
     * @throws IOException when there was an error during connection.
     */
    ConnectorThresholdsResponse getThresholds() throws IOException;

	/**
	 * Returns the accumulators data.
	 * @param accumulatorNames names of accumulators to retrieve.
     * @return {@link ConnectorAccumulatorResponse}
	 */
	ConnectorAccumulatorResponse getAccumulators(List<String> accumulatorNames);

    /**
     * Returns all accumulators names.
     * @return {@link ConnectorAccumulatorsNamesResponse}
     * @throws IOException when there was an error during connection.
     */
    ConnectorAccumulatorsNamesResponse getAccumulatorsNames() throws IOException;

}
