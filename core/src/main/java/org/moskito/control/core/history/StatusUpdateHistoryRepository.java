package org.moskito.control.core.history;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.moskito.control.core.Repository;
import org.moskito.control.core.history.service.HistoryService;
import org.moskito.control.core.history.service.InMemoryHistoryService;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.core.status.StatusChangeListener;

import java.util.List;

/**
 * This repository manages and holds all status updates.
 *
 * @author lrosenberg
 * @since 09.06.13 21:45
 */
public final class StatusUpdateHistoryRepository implements StatusChangeListener {
	private HistoryService historyService;

	private StatusUpdateHistoryRepository(){
		this.historyService = new InMemoryHistoryService();
		Repository.getInstance().getEventsDispatcher().addStatusChangeListener(this);
	}

	@SuppressFBWarnings(value="RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT",
			justification="Called with ignoring of return value to load class")
	public static StatusUpdateHistoryRepository getInstance(){
		return StatusUpdateHistoryRepositoryInstanceHolder.instance;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	public List<StatusUpdateHistoryItem> getHistoryForApplication(){
		return historyService.getItems();
	}

	public List<StatusUpdateHistoryItem> getHistoryForComponents(List<String> componentNames){
		return this.historyService.getItemsByComponentNames(componentNames);
	}

	public List<StatusUpdateHistoryItem> getHistoryForComponent(String componentName) {
		return this.historyService.getItemsByComponentName(componentName);
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
		StatusUpdateHistoryItem historyItem = new StatusUpdateHistoryItem(event.getComponent(), event.getOldStatus(), event.getStatus(), event.getTimestamp());
		historyService.addItem(historyItem);
	}
}
