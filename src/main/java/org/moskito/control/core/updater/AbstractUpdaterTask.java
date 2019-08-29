package org.moskito.control.core.updater;

import org.moskito.control.core.Component;

/**
 * Abstract class as base class for updater tasks.
 *
 * @author lrosenberg
 * @since 21.06.13 13:13
 */
public abstract class AbstractUpdaterTask implements UpdaterTask{
	/**
	 * Assigned component.
	 */
	private Component component;

	/**
	 * Creates a new AbstractUpdaterTask.
	 * @param aComponent the target component.
	 */
	protected AbstractUpdaterTask(Component aComponent){
		component = aComponent;
	}

	Component getComponent() {
		return component;
	}

	@Override public String toString(){
		return "["+getComponent()+"]";
	}

	@Override public String getKey(){
		return getComponent().toString();
	}



}
