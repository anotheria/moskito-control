package org.moskito.control.core.history;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.moskito.control.core.Component;
import org.moskito.control.core.ComponentRepository;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.core.status.StatusChangeListener;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * This repository manages and holds all status updates.
 *
 * @author lrosenberg
 * @since 09.06.13 21:45
 */
public final class StatusUpdateHistoryRepository implements StatusChangeListener {

	private StatusUpdateHistory history = new StatusUpdateHistory();

	private StatusUpdateHistoryRepository(){
		ComponentRepository.getInstance().getEventsDispatcher().addStatusChangeListener(this);
	}

	@SuppressFBWarnings(value="RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT",
			justification="Called with ignoring of return value to load class")
	public static StatusUpdateHistoryRepository getInstance(){
		return StatusUpdateHistoryRepositoryInstanceHolder.instance;
	}


	public List<StatusUpdateHistoryItem> getHistoryForApplication(){
		return history.getItems();
	}

	public List<StatusUpdateHistoryItem> getHistoryForComponents(List<Component> components){
		List<StatusUpdateHistoryItem> all = getHistoryForApplication();
		List<StatusUpdateHistoryItem> ret = new LinkedList<>();
		HashSet<Component> componentsSearchSet = new HashSet();
		componentsSearchSet.addAll(components);

		for (StatusUpdateHistoryItem item : all){
			if (componentsSearchSet.contains(item.getComponent())){
				ret.add(item);
			}
		}

		return ret;
	}

	/**
	 * Singleton instance holder class.
	 */
	private static class StatusUpdateHistoryRepositoryInstanceHolder{
		/**
		 * Singleton instance.
		 */
		private static final StatusUpdateHistoryRepository instance = new StatusUpdateHistoryRepository();
	}


	@Override
	public void notifyStatusChange(StatusChangeEvent event) {
		getHistory().addToHistory(event);
	}

	private StatusUpdateHistory getHistory(){
		return history;
	}
}
