package org.moskito.control.connectors;

import java.util.Map;

/**
 * Utility that creates a connection response parser for specific response.
 *
 * @author lrosenberg
 * @since 15.06.13 12:39
 */
public final class ConnectorResponseParsers {
	/**
	 * Creates a new returns a parser for this response type.
	 * @param reply
	 * @return
	 */
	public static ConnectorResponseParser getParser(Map reply){
		int protocolVersion = ((Double)(reply.get("protocolVersion"))).intValue();

		switch(protocolVersion){
			case 1:
				return new V1Parser();
			default:
				throw new AssertionError("Have no parser for protocol version "+protocolVersion);
		}

	}
}
