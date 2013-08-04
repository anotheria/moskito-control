package org.moskito.control.core.updater;

import net.anotheria.util.NumberUtils;
import org.apache.log4j.Logger;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.config.UpdaterConfig;
import org.moskito.control.connectors.ConnectorResponse;
import org.moskito.control.core.Application;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.Component;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Base class for updaters.
 *
 * @author lrosenberg
 * @since 21.06.13 12:47
 */
abstract class AbstractUpdater<T extends ConnectorResponse> {
	/**
	 * Configuration. Actually we only need the update configuration object from it, but we keep link to the main object in case it got reconfigured on the fly.
	 */
	private MoskitoControlConfiguration configuration = MoskitoControlConfiguration.getConfiguration();

	/**
	 * Logger.
	 */
	private static Logger log = Logger.getLogger(ApplicationStatusUpdater.class);

	/**
	 * The trigger thread that triggers updates.
	 */
	private Thread triggerThread;

	/**
	 * Indicates that previous update isn't finished yet.
	 */
	private AtomicBoolean updateInProgressFlag = new AtomicBoolean(false);

	/**
	 * A map with tasks currently being executed.
	 */
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

	protected AbstractUpdater(){
		triggerThread = new Thread(new UpdateTrigger(this));
		triggerThread.setDaemon(true);
		if (getUpdaterConfig().isEnabled())
			triggerThread.start();
		else
			log.warn("Updater ("+getClass().getSimpleName()+") disabled via config.");

		updaterService = Executors.newFixedThreadPool(getUpdaterConfig().getThreadPoolSize());
		connectorService = Executors.newFixedThreadPool(getUpdaterConfig().getThreadPoolSize());
	}

	abstract UpdaterConfig getUpdaterConfig();

	/**
	 * Returns the configuration.
	 * @return
	 */
	protected MoskitoControlConfiguration getConfiguration(){
		return configuration;
	}

	protected abstract UpdaterTask createTask(Application application, Component component);

	private void triggerUpdate(){
		printInfoAboutExecutorService("updater", (ThreadPoolExecutor)updaterService);
		printInfoAboutExecutorService("connector", (ThreadPoolExecutor)connectorService);

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
			List<Application> applications = ApplicationRepository.getInstance().getApplications();
			for (Application app: applications){
				List<Component> components = app.getComponents();
				for (Component c : components){
					log.debug("Have to update "+app+" - "+c);
					numberOfAppsForUpdate++;

					UpdaterTask task = createTask(app, c);
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
				if (numberOfAppsForUpdate>configuration.getStatusUpdater().getThreadPoolSize()){
					log.warn("Number of apps to update is larger than available threads, consider increasing thread count "+numberOfAppsForUpdate+" > "+configuration.getStatusUpdater().getThreadPoolSize());
				}
			}


		}finally{
			updateInProgressFlag.set(false);
		}



	}

	protected Future<T> submit(Callable task){
		return connectorService.submit(task);
	}





	/**
	 * Triggerer that fires the updates at regular intervals.
	 */
	static class UpdateTrigger implements Runnable{

		/**
		 * Link to the parent updater. Used to trigger the triggerUpdate() method on it.
		 */
		private AbstractUpdater updater;

		public UpdateTrigger(AbstractUpdater anUpdater){
			updater = anUpdater;
		}

		/**
		 * Counts the update runs for debugging purposes.
		 */
		private long runCounter = 1;

		@Override public void run(){
			while(true){
				log.info("Triggering new update run (status) - " + (runCounter++) + " " + NumberUtils.makeISO8601TimestampString());
				updater.triggerUpdate();
				try{
					Thread.sleep(MoskitoControlConfiguration.getConfiguration().getStatusUpdater().getCheckPeriodInSeconds()*1000L);
				}catch(InterruptedException e){
					//ignored for now.
				}
			}
		}
	}

	public void printInfoAboutExecutorService(String poolName, ThreadPoolExecutor executor){
		System.out.println("%%% "+getClass().getSimpleName()+" Pool "+poolName);
		System.out.println("%%% TaskCount "+executor.getTaskCount()+", AC: "+executor.getActiveCount()+", Completed: "+executor.getCompletedTaskCount()+", Pool size: "+executor.getPoolSize());
	}



}
