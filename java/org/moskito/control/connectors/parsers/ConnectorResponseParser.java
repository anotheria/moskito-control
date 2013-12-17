package org.moskito.control.connectors.parsers;

import org.moskito.control.connectors.response.ConnectorAccumulatorResponse;
import org.moskito.control.connectors.response.ConnectorAccumulatorsNamesResponse;
import org.moskito.control.connectors.response.ConnectorStatusResponse;
import org.moskito.control.connectors.response.ConnectorThresholdsResponse;

import java.util.Map;

/**
 * Defines a parser that can parse a connector response. Since the connector response can change over time (new
 * versions) the older version should still be able to become parsed.
 *
 * @author lrosenberg
 * @since 15.06.13 12:36
 */
public interface ConnectorResponseParser {
	/**
	 * Parses the status response json object.
	 * @param serverReply
	 * @return
	 */
	ConnectorStatusResponse parseStatusResponse(Map serverReply);

	/**
	 * Parses the accumulator response json object.
	 * @param serverReply
	 * @return
	 */
	ConnectorAccumulatorResponse parseAccumulatorResponse(Map serverReply);

    /**
     * Parses the thresholds response json object.
     * @param serverReply
     * @return
     */
    ConnectorThresholdsResponse parseThresholdsResponse(Map serverReply);

    /**
     * Parses the accumulators names response json object.
     * @param serverReply
     * @return
     */
    ConnectorAccumulatorsNamesResponse parseAccumulatorsNamesResponse(Map serverReply);

}
