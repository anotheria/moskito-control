package org.moskito.control.core.history;

import org.moskito.control.core.Application;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.Component;
import org.moskito.control.core.Status;
import org.moskito.control.core.StatusChangeListener;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 09.06.13 21:45
 */
public class StatusUpdateHistoryRepository implements StatusChangeListener {

	private StatusUpdateHistoryRepository(){
		ApplicationRepository.getInstance().addStatusChangeListener(this);
	}

	public static StatusUpdateHistoryRepository getInstance(){
		return StatusUpdateHistoryRepositoryInstanceHolder.instance;
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
}
