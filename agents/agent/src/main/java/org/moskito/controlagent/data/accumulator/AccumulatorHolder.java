package org.moskito.controlagent.data.accumulator;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Container for component's accumulators data.
 *
 * @author lrosenberg
 * @since 18.06.13 09:33
 */
public class AccumulatorHolder implements Serializable{

	private static final long serialVersionUID = 1L;

	private String name;
	private List<AccumulatorDataItem> items;

	public AccumulatorHolder(String name){
		this.name = name;
		items = new LinkedList<AccumulatorDataItem>();
	}

	public void addItem(AccumulatorDataItem item){
		items.add(item);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AccumulatorDataItem> getItems() {
		return items;
	}

	public void setItems(List<AccumulatorDataItem> items) {
		this.items = items;
	}
}
