package org.moskito.control.core.updater;

import org.moskito.control.config.ComponentConfig;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.config.UpdaterConfig;
import org.moskito.control.connectors.Connector;
import org.moskito.control.connectors.ConnectorFactory;
import org.moskito.control.connectors.response.ConnectorThresholdsResponse;
import org.moskito.control.core.Application;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * This class handles the systematic thresholds updates. This class works as follows:
 * The updater trigger triggers a new update in defined time intervals and only if the previous update is fulfilled.
 * The update method reads all applications and their components and creates an UpdaterTask for each component.
 * The UpdaterTasks are submitted into updaterService. Upon execution the updaterTask would than create a ConnectorTask
 * and submit it to connectorService. The two tasks are needed to give the updater task possibility to control the
 * execution of the connector and the ability to abort(cancel) it, if the execution lasts too long.
 * Finally, ConnectorTask initializes the connector and attempts to retrieve the new status. This status will be
 * updated in the Component by the UpdaterTask once the ConnectorTask is finished (or got aborted).
 *
 * @author Vladyslav Bezuhlyi
 */
public class ThresholdsUpdater extends AbstractUpdater<ConnectorThresholdsResponse> {

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(ThresholdsUpdater.class);

    /**
     * Returns the singleton instance.
     * @return the one and only.
     */
    public static ThresholdsUpdater getInstance() {
        return AccumulatorsNamesUpdaterInstanceHolder.instance;
    }

    @Override
    UpdaterConfig getUpdaterConfig() {
        return getConfiguration().getThresholdsUpdater();
    }

    @Override
    protected UpdaterTask createTask(Application application, Component component) {
        return new ThresholdsUpdaterTask(application, component);
    }


    /**
     * Singleton instance holder class.
     */
    private static class AccumulatorsNamesUpdaterInstanceHolder {

        /**
         * Singleton instance.
         */
        private static final ThresholdsUpdater instance = new ThresholdsUpdater();


    }


    /**
     * This class represents a single task to be executed by a connector.
     * A task for the connector is to retrieve accumulators' names for a component in an application.
     */
    static class ConnectorTask implements Callable<ConnectorThresholdsResponse> {

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
         * @param application application to connect to.
         * @param component component to connect to.
         */
        public ConnectorTask(Application application, Component component){
            this.application = application;
            this.component = component;
        }


        @Override
        public ConnectorThresholdsResponse call() throws Exception {
            ComponentConfig componentConfig = MoskitoControlConfiguration.getConfiguration().getApplication(application.getName()).getComponent(component.getName());

            Connector connector = ConnectorFactory.createConnector(componentConfig.getConnectorType());
            connector.configure(componentConfig.getLocation());

            ApplicationRepository.getInstance().getApplication(application.getName()).setLastThresholdsUpdaterRun(System.currentTimeMillis());

            return connector.getThresholds();
        }

    }


    /**
     * This class represents a single task to be executed by an updater.
     * Task for thresholds retrieving.
     */
    static class ThresholdsUpdaterTask extends AbstractUpdaterTask implements UpdaterTask {

        /**
         * Creates a new updater task for given application and component.
         * @param application application to update.
         * @param component component to update.
         */
        public ThresholdsUpdaterTask(Application application, Component component) {
            super(application, component);
        }


        @Override
        public void run() {
            log.debug("Starting execution of " + this);
            ConnectorTask connectorTask = new ConnectorTask(getApplication(), getComponent());
            Future<ConnectorThresholdsResponse> future = ThresholdsUpdater.getInstance().submit(connectorTask);

            ConnectorThresholdsResponse response = null;
            try{
                long timeout = ThresholdsUpdater.getInstance().getConfiguration().getThresholdsUpdater().getTimeoutInSeconds();
                response = future.get(timeout, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.warn("Caught exception waiting for execution of "+this+", no thresholds - "+e.getMessage(), e);
            }

            if (!future.isDone()) {
                log.warn("Task still not done after timeout, canceling");
                future.cancel(true);
            }

            if (response == null) {
                log.warn("Got no reply from connector...");
            } else {
                log.info("Got new reply from connector " + response);
                getApplication().setLastThresholdsUpdaterSuccess(System.currentTimeMillis());
                getComponent().setThresholds(response.getItems());
            }

            log.debug("Finished execution of " + this);
        }

    }

}
