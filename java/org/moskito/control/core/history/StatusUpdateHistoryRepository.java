package org.moskito.control.core.history;

import org.moskito.control.core.Application;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.Component;
import org.moskito.control.core.Status;
import org.moskito.control.core.StatusChangeListener;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This repository manages and holds all status updates.
 *
 * @author lrosenberg
 * @since 09.06.13 21:45
 */
public final class StatusUpdateHistoryRepository implements StatusChangeListener {

	private ConcurrentMap<String, StatusUpdateHistory> histories = new ConcurrentHashMap<String, StatusUpdateHistory>();

	private StatusUpdateHistoryRepository(){
		ApplicationRepository.getInstance().addStatusChangeListener(this);
	}

	public static StatusUpdateHistoryRepository getInstance(){
		return StatusUpdateHistoryRepositoryInstanceHolder.instance;
	}


	public List<StatusUpdateHistoryItem> getHistoryForApplication(String applicationName){
		StatusUpdateHistory history = getHistory(applicationName);
		if (history==null)
			return null;//TODO thing about exception here
		return history.getItems();
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
	public void notifyStatusChange(Application app, Component component, Status oldStatus, Status status, long lastUpdateTimestamp) {
		getHistory(app).addToHistory(component, oldStatus, status, lastUpdateTimestamp);
	}

	private StatusUpdateHistory getHistory(String applicationName){
		StatusUpdateHistory h = histories.get(applicationName);
		if (h!=null)
			return h;
		h = new StatusUpdateHistory();
		StatusUpdateHistory old = histories.putIfAbsent(applicationName, h);
		return old == null ? h : old;
	}
	private StatusUpdateHistory getHistory(Application app){
		return getHistory(app.getName());
	}
}
