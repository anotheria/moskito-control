package org.moskito.control.core.updater;

import org.moskito.control.config.ComponentConfig;
import org.moskito.control.config.UpdaterConfig;
import org.moskito.control.connectors.Connector;
import org.moskito.control.connectors.ConnectorFactory;
import org.moskito.control.connectors.response.ConnectorStatusResponse;
import org.moskito.control.core.Component;
import org.moskito.control.core.ComponentRepository;
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
public final class ComponentStatusUpdater extends AbstractUpdater<ConnectorStatusResponse>{

	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(ComponentStatusUpdater.class);

	@Override
	UpdaterConfig getUpdaterConfig() {
		return getConfiguration().getStatusUpdater();
	}

	@Override
	protected UpdaterTask createTask(Component component) {
		return new StatusUpdaterTask(component);
	}

	/**
	 * Returns the singleton instance.
	 * @return the one and only.
	 */
	public static ComponentStatusUpdater getInstance(){
		return ComponentStatusUpdaterInstanceHolder.instance;
	}

	/**
	 * Singleton instance holder class.
	 */
	private static class ComponentStatusUpdaterInstanceHolder{
		/**
		 * Singleton instance.
		 */
		private static final ComponentStatusUpdater instance = new ComponentStatusUpdater();
	}



	/**
	 * This class represents a single task to be executed by a connector. A task for the connector is check of the
	 * status of a component in an application.
	 */
	static class ConnectorTask implements Callable<ConnectorStatusResponse>{

		/**
		 * Target component.
		 */
		private Component component;

		/**
		 * Creates a new connector task.
		 * @param aComponent component to connect to.
		 */
		public ConnectorTask(Component aComponent){
			component = aComponent;
		}


		@Override
		public ConnectorStatusResponse call() throws Exception {
			ComponentConfig cc = component.getConfiguration();
			Connector connector = ConnectorFactory.createConnector(cc.getConnectorType());
			connector.configure(cc.getName(), cc.getLocation(), cc.getCredentials());
			ComponentRepository.getInstance().setLastStatusUpdaterRun(System.currentTimeMillis());
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
		 * @param aComponent component to update.
		 */
		public StatusUpdaterTask(Component aComponent){
			super(aComponent);
		}

		@Override
		public void run(){
			log.debug("Starting execution of "+this);
			Component component = getComponent();
			ConnectorTask task = new ConnectorTask(component);
			Future<ConnectorStatusResponse> reply =  ComponentStatusUpdater.getInstance().submit(task);
			ConnectorStatusResponse response = null;
			try{
				response = reply.get(ComponentStatusUpdater.getInstance().getConfiguration().getStatusUpdater().getTimeoutInSeconds(), TimeUnit.SECONDS);
			} catch(Exception e){
				log.warn("Caught exception waiting for execution of "+this+", no new status - "+e.getMessage());
			}

			if (!reply.isDone()){
				log.warn("Reply still not done after timeout, canceling "+this);
				reply.cancel(true);
			}

			if (response == null) {
				log.warn("Got no reply from connector - " + this);
				ComponentStatusUpdater.getInstance().checkComponentStatus(component, "Can't connect to the " + getComponent().getName()+".");
				return;
			}

			log.info("Got new reply from connector "+response+" - "+this);
			ComponentRepository.getInstance().setLastStatusUpdaterSuccess(System.currentTimeMillis());
			//now celebrate!

			//think about it, actually we have both application and component, so we don't have to look it up.
			//component.setStatus(response.getStatus()) sounds like a healthy alternative.
			component.setStatus(response.getStatus());
			//added with protocol version2 and moskito-2.10.0 - currently executed requests.
			component.setCurrentRequestCount(response.getNowRunningCount());
			log.debug("Finished execution of "+this);
		}
	}

	@Override
	protected String getUpdaterGoal() {
		return "ApplicationStatus";
	}

	@Override
	public void terminate() {
		super.terminate();
	}
}
