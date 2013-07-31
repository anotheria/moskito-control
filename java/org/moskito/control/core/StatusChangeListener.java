package org.moskito.control.core;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 09.06.13 21:47
 */
public interface StatusChangeListener {
	/**
	 * Called whenever a status of a component is changed.
	 * @param app application to which the component belongs.
	 * @param component component that changed the status.
	 * @param oldStatus old status of the component.
	 * @param status new status of the component.
	 * @param lastUpdateTimestamp timestamp at which the status change took place.
	 */
	void notifyStatusChange(Application app, Component component, Status oldStatus, Status status, long lastUpdateTimestamp);
}
