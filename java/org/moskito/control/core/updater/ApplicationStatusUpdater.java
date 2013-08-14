package org.moskito.control.core.updater;

import org.moskito.control.config.ComponentConfig;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.config.UpdaterConfig;
import org.moskito.control.connectors.Connector;
import org.moskito.control.connectors.ConnectorFactory;
import org.moskito.control.connectors.ConnectorStatusResponse;
import org.moskito.control.core.Application;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.Component;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * This class handles the systematic status updates. This class works as follows:
 * The updater trigger triggers a new update in defined time intervals and only if the previous update is fulfilled.
 * The update method reads all applications and their components and creates an UpdaterTask for each component.
 * The UpdaterTasks are submitted into updaterService. Upon execution the updaterTask would than create a ConnectorTask
 * and submit it to connectorService. The two tasks are needed to give the updater task possibility to control the
 * execution of the connector and the ability to abort(cancel) it, if the execution lasts too long.
 * Finally, ConnectorTask initializes the connector and attempts to retrieve the new status. This status will be
 * updated in the Component by the UpdaterTask once the ConnectorTask is finished (or got aborted).
 *
 * @author lrosenberg
 * @since 28.05.13 21:25
 */
public final class ApplicationStatusUpdater extends AbstractUpdater<ConnectorStatusResponse>{

	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(ApplicationStatusUpdater.class);

	@Override
	UpdaterConfig getUpdaterConfig() {
		return getConfiguration().getStatusUpdater();
	}

	@Override
	protected UpdaterTask createTask(Application application, Component component) {
		return new StatusUpdaterTask(application, component);
	}

	/**
	 * Returns the singleton instance.
	 * @return the one and only.
	 */
	public static ApplicationStatusUpdater getInstance(){
		return ApplicationStatusUpdaterInstanceHolder.instance;
	}

	/**
	 * Singleton instance holder class.
	 */
	private static class ApplicationStatusUpdaterInstanceHolder{
		/**
		 * Singleton instance.
		 */
		private static final ApplicationStatusUpdater instance = new ApplicationStatusUpdater();
	}



	/**
	 * This class represents a single task to be executed by a connector. A task for the connector is check of the
	 * status of a component in an application.
	 */
	static class ConnectorTask implements Callable<ConnectorStatusResponse>{
		/**
		 * Target application.
		 */
		private Application application;
		/**
		 * Target component.
		 */
		private Component component;

		/**
		 * Creates a new connector task.
		 * @param anApplication application to connect to.
		 * @param aComponent component to connect to.
		 */
		public ConnectorTask(Application anApplication, Component aComponent){
			application = anApplication;
			component = aComponent;
		}


		@Override
		public ConnectorStatusResponse call() throws Exception {
			ComponentConfig cc = MoskitoControlConfiguration.getConfiguration().getApplication(application.getName()).getComponent(component.getName());
			Connector connector = ConnectorFactory.createConnector(cc.getConnectorType());
			connector.configure(cc.getLocation());
			ApplicationRepository.getInstance().getApplication(application.getName()).setLastStatusUpdaterRun(System.currentTimeMillis());
			ConnectorStatusResponse response = connector.getNewStatus();
			return response;
		}
	}

	/**
	 * Task used as element for the updater executor service.
	 */
	static class StatusUpdaterTask extends AbstractUpdaterTask implements UpdaterTask{
		/**
		 * Creates a new task for given application and component.
		 * @param anApplication application to update.
		 * @param aComponent component to update.
		 */
		public StatusUpdaterTask(Application anApplication, Component aComponent){
			super(anApplication, aComponent);
		}

		@Override
		public void run(){
			log.debug("Starting execution of "+this);
			ConnectorTask task = new ConnectorTask(getApplication(), getComponent());
			Future<ConnectorStatusResponse> reply =  ApplicationStatusUpdater.getInstance().submit(task);
			ConnectorStatusResponse response = null;
			try{
				response = reply.get(ApplicationStatusUpdater.getInstance().getConfiguration().getStatusUpdater().getTimeoutInSeconds(), TimeUnit.SECONDS);
			}catch(Exception e){
				log.warn("Caught exception waiting for execution of "+this+", no new status - "+e.getMessage());
			}

			if (!reply.isDone()){
				log.warn("Reply still not done after timeout, canceling");
				reply.cancel(true);
			}

			if (!reply.isDone() ||response == null){
				log.warn("Got no reply from connector...");
				response = new ConnectorStatusResponse(new Status(HealthColor.PURPLE, "Can't connect to the "+getApplication().getName()+"."+getComponent().getName()));
			}else{
				log.info("Got new reply from connector "+response);
				getApplication().setLastStatusUpdaterSuccess(System.currentTimeMillis());
				//now celebrate!
			}

			//think about it, actually we have both application and component, so we don't have to look it up.
			//component.setStatus(response.getStatus()) sounds like a healthy alternative.
			ApplicationRepository.getInstance().getApplication(getApplication().getName()).getComponent(getComponent().getName()).setStatus(response.getStatus());
			log.debug("Finished execution of "+this);
		}
	}

}
