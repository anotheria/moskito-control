package org.moskito.control.connectors;

import java.util.Map;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 15.06.13 12:36
 */
public interface ConnectorResponseParser {
	ConnectorResponse parseResponse(Map serverReply);
}
