package org.moskito.control.core.history;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.moskito.control.core.Application;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.core.status.StatusChangeListener;

import javax.annotation.CheckReturnValue;
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

	/**
	 * Map for applications and their histories.
	 */
	private ConcurrentMap<String, StatusUpdateHistory> histories = new ConcurrentHashMap<String, StatusUpdateHistory>();

	private StatusUpdateHistoryRepository(){
		ApplicationRepository.getInstance().getEventsDispatcher().addStatusChangeListener(this);
	}

	@SuppressFBWarnings(value="RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT",
			justification="Called with ignoring of return value to load class")
	public static StatusUpdateHistoryRepository getInstance(){
		return StatusUpdateHistoryRepositoryInstanceHolder.instance;
	}


	public List<StatusUpdateHistoryItem> getHistoryForApplication(String applicationName){
		StatusUpdateHistory history = getHistory(applicationName);
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
	public void notifyStatusChange(StatusChangeEvent event) {
		getHistory(event.getApplication()).addToHistory(event);
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
