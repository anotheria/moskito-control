package org.moskito.control.connectors.parsers;

import org.moskito.control.connectors.response.*;

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
	 * @return parsed status response
	 */
	ConnectorStatusResponse parseStatusResponse(Map serverReply);

	/**
	 * Parses the accumulator response json object.
	 * @param serverReply
	 * @return parsed accumulator response
	 */
	ConnectorAccumulatorResponse parseAccumulatorResponse(Map serverReply);

    /**
     * Parses the thresholds response json object.
     * @param serverReply
     * @return parsed threshold response
     */
    ConnectorThresholdsResponse parseThresholdsResponse(Map serverReply);

    /**
     * Parses the accumulators names response json object.
     * @param serverReply
     * @return parsed accumulator names response
     */
    ConnectorAccumulatorsNamesResponse parseAccumulatorsNamesResponse(Map serverReply);

	/**
	 * Parses information response json object
	 * @param serverResponse parsed to map response from server
	 * @return parsed info response
	 */
	ConnectorInformationResponse parseInformationResponse(Map serverResponse);

}
