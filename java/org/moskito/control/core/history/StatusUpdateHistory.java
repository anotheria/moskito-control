package org.moskito.control.core.history;

import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.core.Component;
import org.moskito.control.core.Status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 09.06.13 22:25
 */
public class StatusUpdateHistory {



	private List<StatusUpdateHistoryItem> items = new LinkedList<StatusUpdateHistoryItem>();

	public StatusUpdateHistory(){

	}

	//synchronized because we are modifying the underlying list twice. It should never actually happen from two threads at once,
	//but secure is secure.
	public synchronized void addToHistory(Component component, Status oldStatus, Status status, long updateTimestamp){
		items.add(new StatusUpdateHistoryItem(component, oldStatus, status, updateTimestamp));
		while (items.size()>MoskitoControlConfiguration.getConfiguration().getHistoryItemsAmount()){
			items.remove(0);
		}
	}

	public List<StatusUpdateHistoryItem> getItems(){
		ArrayList<StatusUpdateHistoryItem> ret = new ArrayList<StatusUpdateHistoryItem>();
		ret.addAll(items);
		Collections.reverse(ret);
		return ret;
	}


}
