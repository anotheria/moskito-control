package org.moskito.control.connectors;

import java.util.Map;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 15.06.13 12:39
 */
public final class ConnectorResponseParsers {
	public static final ConnectorResponseParser getParser(Map reply){
		System.out.println("CHECKING FOR PARSER for reply: "+reply);
		int protocolVersion = ((Double)(reply.get("protocolVersion"))).intValue();
		System.out.println("CHECKING FOR PARSER for protocol "+protocolVersion);

		switch(protocolVersion){
			case 1:
				return new V1Parser();
			default:
				throw new AssertionError("Have no parser for protocol version "+protocolVersion);
		}

	}
}
