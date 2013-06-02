package org.anotheria.moskito.control.core.updater;

import net.anotheria.util.NumberUtils;
import org.anotheria.moskito.control.config.MoskitoControlConfiguration;
import org.anotheria.moskito.control.connectors.ConnectorResponse;
import org.anotheria.moskito.control.core.Application;
import org.anotheria.moskito.control.core.ApplicationRepository;
import org.anotheria.moskito.control.core.Component;
import org.anotheria.moskito.control.core.HealthColor;
import org.anotheria.moskito.control.core.Status;
import org.apache.log4j.Logger;

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
 * This class handles the systematic status updates.
 *
 * @author lrosenberg
 * @since 28.05.13 21:25
 */
public class ApplicationStatusUpdater{
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
					System.out.println("Have to update "+app+" - "+c);
					numberOfAppsForUpdate++;

					UpdaterTask task = new UpdaterTask(app, c);
					String taskKey = task.getKey();
					if (currentlyExecutedTasks.get(taskKey)!=null){
						log.warn("UpdaterTask for key "+taskKey+" and task: "+task+" still running, skipped.");
					}else{
						log.info("Submitting check for "+taskKey+" for execution");
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
			return null;  //To change body of implemented methods use File | Settings | File Templates.
		}
	}

	static class UpdaterTask implements Runnable{
		private Application application;
		private Component component;

		public UpdaterTask(Application anApplication, Component aComponent){
			application = anApplication;
			component = aComponent;
		}

		public void run(){
			System.out.println("Starting execution of "+this);
			ConnectorTask task = new ConnectorTask(application, component);
			long startedToWait = System.currentTimeMillis();
			Future<ConnectorResponse> reply =  ApplicationStatusUpdater.getInstance().submit(task);
			ConnectorResponse response = null;
			try{
				response = reply.get(ApplicationStatusUpdater.getInstance().configuration.getUpdater().getTimeoutInSeconds(), TimeUnit.SECONDS);
			}catch(Exception e){
				log.warn("Caught exception waiting for execution of "+this+", no new status.");
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

			ApplicationRepository.getInstance().getApplication(application.getName()).getComponent(component.getName()).setStatus(response.getStatus());
			System.out.println("Finished execution of "+this);
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

		private long runCounter = 1;

		public void run(){
			while(true){
				try{
					Thread.sleep(MoskitoControlConfiguration.getConfiguration().getUpdater().getCheckPeriodInSeconds()*1000L);
				}catch(InterruptedException e){
					//ignored for now.
				}
				log.info("Triggering new update run - "+(runCounter++));
				getInstance().triggerUpdate();
			}
		}
	}

}
