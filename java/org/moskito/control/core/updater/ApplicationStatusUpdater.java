package org.moskito.control.core.updater;

import net.anotheria.util.NumberUtils;
import org.apache.log4j.Logger;
import org.moskito.control.config.ComponentConfig;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.connectors.Connector;
import org.moskito.control.connectors.ConnectorFactory;
import org.moskito.control.connectors.ConnectorResponse;
import org.moskito.control.core.Application;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.Component;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.Status;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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
public final class ApplicationStatusUpdater{
	/**
	 * Configuration. Actually we only need the update configuration object from it, but we keep link to the main object in case it got reconfigured on the fly.
	 */
	MoskitoControlConfiguration configuration = MoskitoControlConfiguration.getConfiguration();

	private static Logger log = Logger.getLogger(ApplicationStatusUpdater.class);

	private Thread triggerThread;

	private AtomicBoolean updateInProgressFlag = new AtomicBoolean(false);

	private ConcurrentMap<String, UpdaterTask> currentlyExecutedTasks = new ConcurrentHashMap<String, UpdaterTask>();

	/**
	 * This executor services triggers updates and cancels them after the timeout.
	 */
	private final ExecutorService updaterService;

	/**
	 * This executor actually executes the connectors.
	 */
	private final ExecutorService connectorService;

	/**
	 * Number of apps for update in last update run, used to determine if the thread pool size should be changed.
	 */
	private int lastNumberOfAppToUpdate = 0;

	private ApplicationStatusUpdater(){
		triggerThread = new Thread(new UpdateTrigger());
		triggerThread.setDaemon(true);
		triggerThread.start();

		updaterService = Executors.newFixedThreadPool(configuration.getUpdater().getThreadPoolSize());
		connectorService = Executors.newFixedThreadPool(configuration.getUpdater().getThreadPoolSize());
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


	private void triggerUpdate(){
		//actually current implementation of the UpdateTrigger(thread) will not allow for multiple execution,
		// but this is nothing for the eternity and the concurrent updates will be forgotten soon.
		if (updateInProgressFlag.get()){
			log.warn("Previous update isn't finished, skipping.");
			return;
		}
		updateInProgressFlag.set(true);
		try{
			//now the update process.
			//build what to update
			int numberOfAppsForUpdate = 0;
			List <Application> applications = ApplicationRepository.getInstance ().getApplications();
			for (Application app: applications){
				List<Component> components = app.getComponents();
				for (Component c : components){
					log.debug("Have to update "+app+" - "+c);
					numberOfAppsForUpdate++;

					UpdaterTask task = new UpdaterTask(app, c);
					String taskKey = task.getKey();
					if (currentlyExecutedTasks.get(taskKey)!=null){
						log.warn("UpdaterTask for key "+taskKey+" and task: "+task+" still running, skipped.");
					}else{
						log.debug("Submitting check for " + taskKey + " for execution");
						updaterService.execute(task);
					}
				}

			}

			if (numberOfAppsForUpdate!=lastNumberOfAppToUpdate){
				lastNumberOfAppToUpdate = numberOfAppsForUpdate;
				if (numberOfAppsForUpdate>configuration.getUpdater().getThreadPoolSize()){
					log.warn("Number of apps to update is larger than available threads, consider increasing thread count "+numberOfAppsForUpdate+" > "+configuration.getUpdater().getThreadPoolSize());
				}
			}


		}finally{
			updateInProgressFlag.set(false);
		}



	}

	Future<ConnectorResponse> submit(ConnectorTask task){
		return connectorService.submit(task);
	}

	static class ConnectorTask implements Callable<ConnectorResponse>{

		private Application application;
		private Component component;

		public ConnectorTask(Application anApplication, Component aComponent){
			application = anApplication;
			component = aComponent;
		}


		@Override
		public ConnectorResponse call() throws Exception {
			ComponentConfig cc = MoskitoControlConfiguration.getConfiguration().getApplication(application.getName()).getComponent(component.getName());
			Connector connector = ConnectorFactory.createConnector(cc.getConnectorType());
			connector.configure(cc.getLocation());
			ConnectorResponse response = connector.getNewStatus();
			return response;
		}
	}

	/**
	 * Task used as element for the updater executor service.
	 */
	static class UpdaterTask implements Runnable{
		/**
		 * Assigned application.
		 */
		private Application application;
		/**
		 * Assigned component.
		 */
		private Component component;

		/**
		 * Creates a new task for given application and component.
		 * @param anApplication
		 * @param aComponent
		 */
		public UpdaterTask(Application anApplication, Component aComponent){
			application = anApplication;
			component = aComponent;
		}

		@Override
		public void run(){
			log.debug("Starting execution of "+this);
			ConnectorTask task = new ConnectorTask(application, component);
			long startedToWait = System.currentTimeMillis();
			Future<ConnectorResponse> reply =  ApplicationStatusUpdater.getInstance().submit(task);
			ConnectorResponse response = null;
			try{
				response = reply.get(ApplicationStatusUpdater.getInstance().configuration.getUpdater().getTimeoutInSeconds(), TimeUnit.SECONDS);
			}catch(Exception e){
				log.warn("Caught exception waiting for execution of "+this+", no new status.", e);
			}

			if (!reply.isDone()){
				log.warn("Reply still not done after timeout, canceling");
				reply.cancel(true);
			}

			if (!reply.isDone() ||response == null){
				log.warn("Got no reply from connector...");
				response = new ConnectorResponse(new Status(HealthColor.PURPLE, "Can't connect to the "+application.getName()+"."+component.getName()+" @ "+ NumberUtils.makeISO8601TimestampString(System.currentTimeMillis())));
			}else{
				log.info("Got new reply from connector "+response);
				//now celebrate!
			}

			//think about it, actually we have both application and component, so we don't have to look it up.
			//component.setStatus(response.getStatus()) sounds like a healthy alternative.
			ApplicationRepository.getInstance().getApplication(application.getName()).getComponent(component.getName()).setStatus(response.getStatus());
			log.debug("Finished execution of "+this);
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

		public String getKey(){
			return getApplication()+"-"+getComponent();
		}
	}

	/**
	 * Triggerer that fires the updates at regular intervals.
	 */
	static class UpdateTrigger implements Runnable{

		/**
		 * Counts the update runs for debugging purposes.
		 */
		private long runCounter = 1;

		@Override public void run(){
			while(true){
				log.info("Triggering new update run - " + (runCounter++) + " " + NumberUtils.makeISO8601TimestampString());
				getInstance().triggerUpdate();
				try{
					Thread.sleep(MoskitoControlConfiguration.getConfiguration().getUpdater().getCheckPeriodInSeconds()*1000L);
				}catch(InterruptedException e){
					//ignored for now.
				}
			}
		}
	}

}
