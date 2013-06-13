package org.moskito.control.core;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 09.06.13 21:47
 */
public interface StatusChangeListener {
	void notifyStatusChange(Application app, Component component, Status oldStatus, Status status, long lastUpdateTimestamp);
}
