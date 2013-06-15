package org.moskito.control.connectors;

import org.moskito.control.core.HealthColor;
import org.moskito.control.core.Status;

import java.util.List;
import java.util.Map;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 15.06.13 12:39
 */
public class V1Parser implements ConnectorResponseParser{
	@Override
	public ConnectorResponse parseResponse(Map serverReply) {
		Map reply = (Map) serverReply.get("reply");
		Status status = new Status();
		status.setHealth(HealthColor.valueOf((String)reply.get("status")));

		List thresholds = (List)reply.get("thresholds");
		for (Object t : thresholds){
			Map messageMap = (Map)t;
			String message = messageMap.get("threshold")+": "+messageMap.get("value");
			status.addMessage(message);
		}


		return new ConnectorResponse(status);
	}
}
