package org.moskito.control.connectors;

import org.moskito.control.core.AccumulatorDataItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains accumulator data.
 *
 * @author lrosenberg
 * @since 13.07.13 07:52
 */
public class ConnectorAccumulatorResponse extends ConnectorResponse{
	/**
	 * Map with a list of data for each accumulator.
	 */
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

	@Override public String toString(){
		return getClass().getSimpleName()+" "+(data == null ? "null" : ""+data.size()+" elements ("+getMapInfo()+")");
	}

	private String getMapInfo(){
		StringBuilder ret = new StringBuilder();
		for (Map.Entry<String,List<AccumulatorDataItem>> entry : data.entrySet()){
			ret.append(entry.getValue().size()).append(" ");
		}

		return ret.toString();
	}
}
