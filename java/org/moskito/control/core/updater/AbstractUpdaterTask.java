package org.moskito.control.core.updater;

import org.moskito.control.core.Application;
import org.moskito.control.core.Component;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 21.06.13 13:13
 */
public abstract class AbstractUpdaterTask implements UpdaterTask{
	/**
	 * Assigned application.
	 */
	private Application application;
	/**
	 * Assigned component.
	 */
	private Component component;


	protected AbstractUpdaterTask(Application anApplication, Component aComponent){
		application = anApplication;
		component = aComponent;
	}
	Application getApplication() {
		return application;
	}

	Component getComponent() {
		return component;
	}

	@Override public String toString(){
		return getApplication()+"-"+getComponent();
	}

	@Override public String getKey(){
		return getApplication()+"-"+getComponent();
	}



}
