package org.anotheria.moskito.control.core.updater;

import org.anotheria.moskito.control.config.MoskitoControlConfiguration;
import org.anotheria.moskito.control.core.Application;
import org.anotheria.moskito.control.core.ApplicationRepository;
import org.anotheria.moskito.control.core.Component;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

	/**
	 * This executor services triggers updates and cancels them after the timeout.
	 */
	private ExecutorService updaterService;

	/**
	 * This executor actually executes the connectors.
	 */
	private ExecutorService connectorService;

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

	static class UpdaterTask{

	}

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
