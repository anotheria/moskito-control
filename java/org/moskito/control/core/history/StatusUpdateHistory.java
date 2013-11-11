package org.moskito.control.core.history;

import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.core.status.StatusChangeEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * History of the status changes of an application.
 *
 * @author lrosenberg
 * @since 09.06.13 22:25
 */
public class StatusUpdateHistory {

	/**
	 * List with history items.
	 */
	private List<StatusUpdateHistoryItem> items = new LinkedList<StatusUpdateHistoryItem>();

	public StatusUpdateHistory(){

	}

	//synchronized because we are modifying the underlying list twice. It should never actually happen from two threads at once,
	//but secure is secure.
	public synchronized void addToHistory(StatusChangeEvent event){
		items.add(new StatusUpdateHistoryItem(event.getComponent(), event.getOldStatus(), event.getStatus(), event.getTimestamp()));
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

	@Override public String toString(){
		return items.toString();
	}

}
