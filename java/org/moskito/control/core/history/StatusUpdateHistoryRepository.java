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
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 09.06.13 21:45
 */
public class StatusUpdateHistoryRepository implements StatusChangeListener {

	private ConcurrentMap<String, StatusUpdateHistory> histories = new ConcurrentHashMap<String, StatusUpdateHistory>();

	private StatusUpdateHistoryRepository(){
		ApplicationRepository.getInstance().addStatusChangeListener(this);
	}

	public static StatusUpdateHistoryRepository getInstance(){
		return StatusUpdateHistoryRepositoryInstanceHolder.instance;
	}


	public List<StatusUpdateHistoryItem> getHistoryForApplication(String applicationName){
		return null;
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
		System.out.println("Status change in app "+app+", component: "+component+", "+oldStatus+" -> "+status);
	}

	private StatusUpdateHistory getHistory(Application app){
		StatusUpdateHistory h = histories.get(app.getName());
		if (h!=null)
			return h;
		h = new StatusUpdateHistory();
		StatusUpdateHistory old = histories.putIfAbsent(app.getName(), h);
		return old == null ? h : old;
	}
}
