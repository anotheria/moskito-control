package org.moskito.control.connectors;

import org.moskito.control.config.HeaderParameter;
import org.moskito.control.config.HttpMethodType;
import org.moskito.control.config.ComponentConfig;
import org.moskito.control.connectors.response.ConnectorAccumulatorResponse;
import org.moskito.control.connectors.response.ConnectorAccumulatorsNamesResponse;
import org.moskito.control.connectors.response.ConnectorConfigResponse;
import org.moskito.control.connectors.response.ConnectorInformationResponse;
import org.moskito.control.connectors.response.ConnectorNowRunningResponse;
import org.moskito.control.connectors.response.ConnectorStatusResponse;
import org.moskito.control.connectors.response.ConnectorThresholdsResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Connector.
 *
 * @author lrosenberg
 * @since 26.02.13 18:44
 */
public interface Connector {

	void configure(ComponentConfig connectorConfig);

	/**
	 * Returns the status of the application.
	 * @return {@link ConnectorStatusResponse}
	 */
	ConnectorStatusResponse getNewStatus();

    /**
     * Returns the threshold data.
     * @return {@link ConnectorThresholdsResponse}
     * @throws ConnectorException when there was an error during connection.
     */
    ConnectorThresholdsResponse getThresholds();

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

	/**
	 * Returns map, that contains additional information about
	 * this connection.
	 * Each classes, that implements may define
	 * they own data fields.
	 * See connectors documentation to find out witch data
	 * the return.
	 *
	 * @return map with connection data
	 */
	ConnectorInformationResponse getInfo();

	/**
	 * Indicates, is this connector can
	 * supply additional information
	 * about connection.
	 *
	 * Actually this means,
	 * is method {@link Connector#getInfo()}
	 * really returns some useful data or
	 * kind of stub object.
	 *
	 * @return true - overwritten method {@link Connector#getInfo()}
	 * 				  of this connection class can be used.
	 *
	 * 	       false - {@link Connector#getInfo()} do not return any meaningful data
	 */
	boolean supportsInfo();

	/**
	* Indicates, is this connector can supply
	* thresholds from connection.
	*
	* Actually this means,
	* is method {@link Connector#getThresholds()}
	* really work, or return some kind of stub, instead
	* of useful data.
	*
	* @return true - overwritten method {@link Connector#getThresholds()}
	* 				  of this connection class can be used.
	*
	* 	       false - {@link Connector#getThresholds()} do not return any meaningful data
	*/
	boolean supportsThresholds();

	/**
	 * Indicates, is this connector can supply
	 * accumulators from connection.
	 *
	 * Actually this means,
	 * is method {@link Connector#getAccumulators(List)}
	 * really work, or return some kind of stub, instead
	 * of useful data.
	 *
	 * @return true - overwritten method {@link Connector#getAccumulators(List)}
	 * 				  of this connection class can be used.
	 *
	 * 	       false - {@link Connector#getAccumulators(List)} do not return any meaningful data
	 */
	boolean supportsAccumulators();

	/**
	 * Indicates if connector can supply component's config.
	 *
	 * @return true if component's config is supported by connector, false - otherwise
	 */
	boolean supportsConfig();

	/**
	 * Indicates if the connector can request now running requests.
	 * @return
	 */
	boolean supportsNowRunning();

	/**
	 * Return now running from this connector. Always check supportsNowRunning() before calling this method.
	 * @return
	 */
	ConnectorNowRunningResponse getNowRunning();

	/**
	 * Returns component's config.
	 *
	 * @return {@link ConnectorConfigResponse} if connector supports it, null - otherwise
	 */
	ConnectorConfigResponse getConfig();
}
