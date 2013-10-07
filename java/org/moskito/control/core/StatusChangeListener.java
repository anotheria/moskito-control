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
     * @param event status change event
     */
	void notifyStatusChange(StatusChangeEvent event);
}
