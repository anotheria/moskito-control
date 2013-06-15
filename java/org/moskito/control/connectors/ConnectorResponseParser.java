package org.moskito.control.connectors;

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
	 * Parses the response json object.
	 * @param serverReply
	 * @return
	 */
	ConnectorResponse parseResponse(Map serverReply);
}
