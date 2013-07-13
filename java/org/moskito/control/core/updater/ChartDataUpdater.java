package org.moskito.control.core.updater;

import org.apache.log4j.Logger;
import org.moskito.control.config.ComponentConfig;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.config.UpdaterConfig;
import org.moskito.control.connectors.Connector;
import org.moskito.control.connectors.ConnectorAccumulatorResponse;
import org.moskito.control.connectors.ConnectorFactory;
import org.moskito.control.core.Application;
import org.moskito.control.core.Chart;
import org.moskito.control.core.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * This class handles the systematic charts updates. This class works as follows:
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
public final class ChartDataUpdater extends AbstractUpdater<ConnectorAccumulatorResponse>{

	private static Logger log = Logger.getLogger(ChartDataUpdater.class);

	@Override
	UpdaterConfig getUpdaterConfig() {
		return getConfiguration().getChartsUpdater();
	}

	@Override
	protected UpdaterTask createTask(Application application, Component component) {
		return new ChartUpdaterTask(application, component);
	}

	/**
	 * Returns the singleton instance.
	 * @return the one and only.
	 */
	public static ChartDataUpdater getInstance(){
		return ChartDataUpdaterInstanceHolder.instance;
	}

	/**
	 * Singleton instance holder class.
	 */
	private static class ChartDataUpdaterInstanceHolder{
		/**
		 * Singleton instance.
		 */
		private static final ChartDataUpdater instance = new ChartDataUpdater();
	}



	/**
	 * This class represents a single task to be executed by a connector. A task for the connector is check of the
	 * status of a component in an application.
	 */
	static class ConnectorTask implements Callable<ConnectorAccumulatorResponse>{
		/**
		 * Target application.
		 */
		private Application application;
		/**
		 * Target component.
		 */
		private Component component;
		/**
		 * Accumulator names.
		 */
		private List<String> accumulatorNames;

		/**
		 * Creates a new connector task.
		 * @param anApplication
		 * @param aComponent
		 * @param someAccumulatorNames
		 */
		public ConnectorTask(Application anApplication, Component aComponent, List<String> someAccumulatorNames){
			application = anApplication;
			component = aComponent;
			accumulatorNames = someAccumulatorNames;
		}


		@Override
		public ConnectorAccumulatorResponse call() throws Exception {
			ComponentConfig cc = MoskitoControlConfiguration.getConfiguration().getApplication(application.getName()).getComponent(component.getName());
			Connector connector = ConnectorFactory.createConnector(cc.getConnectorType());
			connector.configure(cc.getLocation());
			ConnectorAccumulatorResponse response = connector.getAccumulators(accumulatorNames);
			return response;
		}
	}

	/**
	 * Task used as element for the updater executor service.
	 */
	static class ChartUpdaterTask extends AbstractUpdaterTask implements UpdaterTask{
		/**
		 * Creates a new task for given application and component.
		 * @param anApplication
		 * @param aComponent
		 */
		public ChartUpdaterTask(Application anApplication, Component aComponent){
			super(anApplication, aComponent);
		}

		@Override
		public void run(){
			log.debug("Starting execution of "+this);
			System.out.println("--- CHECKING for "+getApplication()+" "+getComponent());
			List<Chart> charts = getApplication().getCharts();
			if (charts==null || charts.size()==0){
				System.out.println("Nothing to do for application "+getApplication());
				log.debug("nothing to do for "+getApplication());
				return;
			}

			List<String> accToGet = new LinkedList<String>();
			for (Chart c : charts){
				System.out.println("have to get data for "+c);
				List<String> accumulatorsForComponent = c.getNeededAccumulatorsForComponent(getComponent().getName());
				System.out.println("for "+getComponent()+" acc: "+accumulatorsForComponent);
				accToGet.addAll(accumulatorsForComponent);



			}

			System.out.println("For app "+getApplication().getName()+" and comp: "+getComponent().getName()+" -> "+accToGet);
			if (accToGet==null || accToGet.size()==0){
				log.debug("Nothing to do for "+this+", skipping.");
				System.out.println("Nothing to do for "+this+", skipping.");
				return;
			}
			ConnectorTask task = new ConnectorTask(getApplication(), getComponent(), accToGet);
			long startedToWait = System.currentTimeMillis();
			Future<ConnectorAccumulatorResponse> reply =  ChartDataUpdater.getInstance().submit(task);
			ConnectorAccumulatorResponse response = null;
			try{
				response = reply.get(ChartDataUpdater.getInstance().getConfiguration().getStatusUpdater().getTimeoutInSeconds(), TimeUnit.SECONDS);
			}catch(Exception e){
				log.warn("Caught exception waiting for execution of "+this+", no new status - "+e.getMessage());
			}

			if (!reply.isDone()){
				log.warn("Reply still not done after timeout, canceling");
				reply.cancel(true);
			}

			if (!reply.isDone() ||response == null){
				log.warn("Got no reply from connector...");
				//TODO do something?
			}else{
				log.info("Got new reply from connector "+response);
				//now celebrate!
			}

			//think about it, actually we have both application and component, so we don't have to look it up.
			//component.setStatus(response.getStatus()) sounds like a healthy alternative.
			//ApplicationRepository.getInstance().getApplication(getApplication().getName()).getComponent(getComponent().getName()).setStatus(response.getStatus());
			log.debug("Finished execution of "+this);
		}
	}

}

