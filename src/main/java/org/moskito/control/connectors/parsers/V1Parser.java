package org.moskito.control.connectors.parsers;

import net.anotheria.moskito.core.threshold.ThresholdStatus;
import org.moskito.control.connectors.response.*;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.accumulator.AccumulatorDataItem;
import org.moskito.control.core.status.Status;
import org.moskito.control.core.threshold.ThresholdDataItem;

import java.util.*;

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

	@Override public ConnectorAccumulatorResponse parseAccumulatorResponse(Map serverReply) {
		ConnectorAccumulatorResponse ret = new ConnectorAccumulatorResponse();
		Map reply = (Map) serverReply.get("reply");

		Collection values = reply.values();
		for (Object replyValue : values){
			Map mapForKey = (Map)replyValue;
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

    @Override
    public ConnectorThresholdsResponse parseThresholdsResponse(Map serverReply) {
        List<ThresholdDataItem> items = new LinkedList<ThresholdDataItem>();

        List<Map> thresholdsReply = (List<Map>) serverReply.get("reply");
        for (Map replyItem : thresholdsReply) {
            ThresholdDataItem item = new ThresholdDataItem();
            item.setName((String) replyItem.get("name"));
            try{
            	item.setStatus(HealthColor.getHealthColor(ThresholdStatus.valueOf((String) replyItem.get("status"))));
			}catch(NullPointerException e){
            	//this exception means that the transmitted status was null
				item.setStatus(HealthColor.NONE);
			}
            item.setLastValue((String) replyItem.get("lastValue"));
            item.setStatusChangeTimestamp(((Double) replyItem.get("statusChangeTimestamp")).longValue());
            items.add(item);
        }

        return new ConnectorThresholdsResponse(items);
    }

    @Override
    public ConnectorAccumulatorsNamesResponse parseAccumulatorsNamesResponse(Map serverReply) {
        List<String> names = new ArrayList<String>();
        List<Map> reply = (List) serverReply.get("reply");

        for (Map replyItem : reply) {
            names.add((String)replyItem.get("name"));
        }

        return new ConnectorAccumulatorsNamesResponse(names);
    }

	/**
	 * Used to get some value from server reply
	 * map. Casts it to String.
	 *
	 * @param map reply map
	 * @param key key to get value
	 * @param type value type. Casting of value depends on it type
	 * @return string representation of value from map or "value not present" string if
	 * 			entry with such key not contains in reply map
	 */
	private String safeGetReplyMapValue(Map map, Object key, ResponseValueType type){

		Object value = map.get(key);

		if(value == null)
			return "value not present";

		switch (type) {

			case TYPE_LONG:
				// gson always parses numbers as double
				// so it be good to remove fractional part from number
				// if it need to be long
				return String.valueOf(((Double) value).longValue());
			case TYPE_DOUBLE:
			case TYPE_STRING:
			default:
				return String.valueOf(value);

		}

	}

	@Override
	public ConnectorInformationResponse parseInformationResponse(Map serverResponse) {

		Map<String, String> infoMap = new HashMap<>();
		ConnectorInformationResponse response = new ConnectorInformationResponse();

		Map reply = (Map) serverResponse.get("reply");

		infoMap.put("JVM Version",
				safeGetReplyMapValue(reply, "javaVersion", ResponseValueType.TYPE_STRING)
		);
		infoMap.put("PID",
				safeGetReplyMapValue(reply, "pid", ResponseValueType.TYPE_LONG)
		);
		infoMap.put("Start Command",
				safeGetReplyMapValue(reply, "startCommand", ResponseValueType.TYPE_STRING)
		);
		infoMap.put("Machine Name",
				safeGetReplyMapValue(reply, "machineName", ResponseValueType.TYPE_STRING)
		);
		infoMap.put("Uptime",
				safeGetReplyMapValue(reply, "uptime", ResponseValueType.TYPE_LONG)
		);
		infoMap.put("Uphours",
				safeGetReplyMapValue(reply, "uphours", ResponseValueType.TYPE_DOUBLE)
		);
		infoMap.put("Updays",
				safeGetReplyMapValue(reply, "updays", ResponseValueType.TYPE_DOUBLE)
		);

		response.setInfo(infoMap);

		return response;

	}

	private enum ResponseValueType {
		TYPE_LONG,
		TYPE_DOUBLE,
		TYPE_STRING
	}


}
