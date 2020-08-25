package org.moskito.control.core.updater;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

import org.moskito.control.common.Status;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.config.UpdaterConfig;
import org.moskito.control.connectors.response.ConnectorResponse;
import org.moskito.control.core.Component;
import org.moskito.control.core.ComponentRepository;
import org.moskito.control.common.HealthColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.anotheria.util.NumberUtils;

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
	private static Logger log = LoggerFactory.getLogger(AbstractUpdater.class);

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
	 * Number of components for update in last update run, used to determine if the thread pool size should be changed.
	 */
	private int lastNumberOfComponentsToUpdate = 0;

	protected AbstractUpdater(){
		triggerThread = new Thread(new UpdateTrigger(this), getClass().getSimpleName()+"-Trigger");
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
	 * @return moskito control main configuration
	 */
	protected MoskitoControlConfiguration getConfiguration(){
		return configuration;
	}

	/**
	 * Creates a new specific updater task.
	 * @param component target component.
	 * @return new task
	 */
	protected abstract UpdaterTask createTask(Component component);

	private void triggerUpdate(){
		printInfoAboutExecutorService("updater", (ThreadPoolExecutor)updaterService);
		printInfoAboutExecutorService("connector", (ThreadPoolExecutor)connectorService);

		//actually current implementation of the UpdateTrigger(thread) will not allow for multiple execution,
		// but this is nothing for the eternity and the concurrent updates will be forgotten soon.
		if (updateInProgressFlag.compareAndSet(false, true)) {
			log.warn("Previous update isn't finished, skipping.");
			return;
		}

		try{
			//now the update process.
			//build what to update
			int numberOfComponentsForUpdate = 0;
			List<Component> components = ComponentRepository.getInstance().getComponents();
			for (Component c : components){
				if (c.isDynamic()){
					checkComponentStatus(c, "Dynamic component: " + c + " made no push");
					continue;
				}
				log.debug("Have to update "+c);
				numberOfComponentsForUpdate++;

				UpdaterTask task = createTask(c);
				String taskKey = task.getKey();
				if (currentlyExecutedTasks.get(taskKey)!=null){
					log.warn("UpdaterTask for key "+taskKey+" and task: "+task+" still running, skipped.");
				}else{
					log.debug("Submitting check for " + taskKey + " for execution");
					updaterService.execute(task);
				}
			}


			if (numberOfComponentsForUpdate!=lastNumberOfComponentsToUpdate){
				lastNumberOfComponentsToUpdate = numberOfComponentsForUpdate;
				if (numberOfComponentsForUpdate>configuration.getStatusUpdater().getThreadPoolSize()){
					log.warn("Number of apps to update is larger than available threads, consider increasing thread count "+numberOfComponentsForUpdate+" > "+configuration.getStatusUpdater().getThreadPoolSize());
				}
			}

		}finally{
			updateInProgressFlag.set(false);
		}



	}
	protected Future<T> submit(Callable<T> task){
		return connectorService.submit(task);
	}

	/**
	 * This is a descriptive method allowing the base class to provide more information.
	 * @return update information
	 */
	protected abstract String getUpdaterGoal();

	public void checkComponentStatus(Component component, String msg) {
		long timeout = MoskitoControlConfiguration.getConfiguration().getComponentStatusTimeoutInSeconds();
		if (System.currentTimeMillis() - component.getLastUpdateTimestamp() > timeout * 1000L){
			component.setStatus(new Status(HealthColor.RED, msg));
		}
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
			while(!Thread.interrupted()){
				log.info("Triggering new update run ("+updater.getUpdaterGoal()+") - " + (runCounter++) + " " + NumberUtils.makeISO8601TimestampString());
				updater.triggerUpdate();
				try{
					Thread.sleep(updater.getUpdaterConfig().getCheckPeriodInSeconds()*1000L);
				}catch(InterruptedException e){
					//ignored for now.
				}
			}
		}
	}

	public void printInfoAboutExecutorService(String poolName, ThreadPoolExecutor executor){
		//do nothing.
		//System.out.println("%%% "+getClass().getSimpleName()+" Pool "+poolName);
		//System.out.println("%%% "+getExecutorStatus(executor));
	}


	public UpdaterStatus getStatus(){
		UpdaterStatus status = new UpdaterStatus();

		status.setUpdateInProgress(updateInProgressFlag.get());
		status.setConnectorStatus(getExecutorStatus((ThreadPoolExecutor)connectorService));
		status.setUpdaterStatus(getExecutorStatus((ThreadPoolExecutor)updaterService));

		return status;
	}

	private ExecutorStatus getExecutorStatus(ThreadPoolExecutor executor){
		ExecutorStatus ret = new ExecutorStatus();
		if (executor==null){
			ret.setPoolSize(-1);
			return ret;
		}
		ret.setActiveCount(executor.getActiveCount());
		ret.setTaskCount(executor.getTaskCount());
		ret.setCompletedTaskCount(executor.getCompletedTaskCount());
		ret.setPoolSize(executor.getPoolSize());
		return ret;
	}

	/**
	 * Terminate executorService instances to allow webapp container stop.
	 */
	protected void terminate() {
		connectorService.shutdown();
		updaterService.shutdown();
	}

}
