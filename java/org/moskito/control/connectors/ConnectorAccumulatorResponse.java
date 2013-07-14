package org.moskito.control.connectors;

import org.moskito.control.core.AccumulatorDataItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 13.07.13 07:52
 */
public class ConnectorAccumulatorResponse extends ConnectorResponse{
	private Map<String, List<AccumulatorDataItem>> data = new HashMap<String, List<AccumulatorDataItem>>();

	public void addDataLine(String name, List<AccumulatorDataItem> line){
		data.put(name, line);
	}

	public Collection<String> getNames(){
		return data.keySet();
	}

	public List<AccumulatorDataItem> getLine(String name){
		return data.get(name);
	}
}
