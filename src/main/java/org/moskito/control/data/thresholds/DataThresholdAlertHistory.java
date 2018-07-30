package org.moskito.control.data.thresholds;

import org.moskito.control.config.MoskitoControlConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class contains all generated alerts at runtime. This class is an one-value-enum-singleton described by J. Bloch.
 */
public enum  DataThresholdAlertHistory {
	/**
	 * Singleton instance.
	 */
	INSTANCE;

	/**
	 * Max history size.
	 * */
	int historySize = MoskitoControlConfiguration.getConfiguration().getHistoryItemsAmount();

	/**
	 * List of alerts.
	 */
	private Queue<DataThresholdAlert> alerts = new LinkedBlockingQueue<>(historySize);


	/**
	 * Adds a new alert. If the number of totally saved alerts is greater than historySize - removes the oldest one.
	 */
	public void addAlert(DataThresholdAlert alert) {
		if (!alerts.offer(alert)) {
			alerts.poll();
			alerts.offer(alert);
		}
	}
	
	/**
	 * Returns the alerts sofar.
	 */
	public List<DataThresholdAlert> getAlerts(){
		List<DataThresholdAlert> ret = new ArrayList<>(alerts);
		Collections.reverse(ret);
		return ret;
	}

	/**
	 * For unit tests
	 * */
	public void clear(){
		alerts = new LinkedBlockingQueue<>(historySize);
	}
}
