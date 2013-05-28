package org.anotheria.moskito.control.core.updater;

import org.anotheria.moskito.control.config.MoskitoControlConfiguration;
import org.apache.log4j.Logger;

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

	private ApplicationStatusUpdater(){
		triggerThread = new Thread(new UpdateTrigger());
		triggerThread.setDaemon(true);
		triggerThread.start();
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
			}
		}
	}

}
