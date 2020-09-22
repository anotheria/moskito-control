package org.moskito.control.connectors.parsers;

import org.moskito.control.connectors.response.ConnectorStatusResponse;

import java.util.Map;

/**
 * V2 parser. The difference between V1 and V2 is support for now running count in the status response.
 *
 * @author lrosenberg
 * @since 22.09.20 10:22
 */
public class V2Parser extends V1Parser {
	@Override
	public ConnectorStatusResponse parseStatusResponse(Map serverReply) {
		ConnectorStatusResponse ret = super.parseStatusResponse(serverReply);

		Map reply = (Map) serverReply.get("reply");
		int nowRunning = 0;
		try {
			nowRunning = Integer.parseInt("" + reply.get("nowRunning"));
		} catch (Exception any) {
			//ignore?
		}
		ret.setNowRunningCount(nowRunning);
		return ret;
	}
}
