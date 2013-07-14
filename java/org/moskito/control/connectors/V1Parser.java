package org.moskito.control.connectors;

import org.moskito.control.core.AccumulatorDataItem;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * JSON Connector Response parser. Supports version1 of the protocol.
 *
 * @author lrosenberg
 * @since 15.06.13 12:39
 */
public class V1Parser implements ConnectorResponseParser{
	@Override
	public ConnectorStatusResponse parseStatusResponse(Map serverReply) {
		Map reply = (Map) serverReply.get("reply");
		Status status = new Status();
		status.setHealth(HealthColor.valueOf((String)reply.get("status")));

		List thresholds = (List)reply.get("thresholds");
		for (Object t : thresholds){
			Map messageMap = (Map)t;
			String message = messageMap.get("threshold")+": "+messageMap.get("value");
			status.addMessage(message);
		}


		return new ConnectorStatusResponse(status);
	}

	@Override public ConnectorAccumulatorResponse parseAccumulatorResponse(Map serverReply){
		ConnectorAccumulatorResponse ret = new ConnectorAccumulatorResponse();
		Map reply = (Map) serverReply.get("reply");

		Set<String> keys = reply.keySet();
		for (String key : keys){
			Map mapForKey = (Map)reply.get(key);
			String name = (String)mapForKey.get("name");
			List<Map> items = (List)mapForKey.get("items");
			ArrayList<AccumulatorDataItem> parsedItems = new ArrayList<AccumulatorDataItem>();
			for (Map m : items){
				AccumulatorDataItem item = new AccumulatorDataItem(((Double)m.get("timestamp")).longValue(), (String)m.get("value"));
				parsedItems.add(item);
			}
			ret.addDataLine(name, parsedItems);

		}
		return ret;
	}
}
