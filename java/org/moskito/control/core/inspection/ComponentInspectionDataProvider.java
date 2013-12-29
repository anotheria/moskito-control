package org.moskito.control.core.inspection;

import org.moskito.control.config.ComponentConfig;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.connectors.Connector;
import org.moskito.control.connectors.ConnectorFactory;
import org.moskito.control.connectors.response.ConnectorAccumulatorResponse;
import org.moskito.control.connectors.response.ConnectorAccumulatorsNamesResponse;
import org.moskito.control.connectors.response.ConnectorThresholdsResponse;
import org.moskito.control.core.Application;
import org.moskito.control.core.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Provides the most recent data for separate section of component on-demand.
 *
 * @author Vladyslav Bezuhlyi
 */
public class ComponentInspectionDataProvider {

    /**
     * Logger.
     */
    Logger log = LoggerFactory.getLogger(ComponentInspectionDataProvider.class);


    /**
     * Provides thresholds data.
     *
     * @param application {@link Application}
     * @param component {@link Component}
     *
     * @return {@link ConnectorThresholdsResponse}
     */
    public ConnectorThresholdsResponse provideThresholds(Application application, Component component) {
        Connector connector = getConfiguredConnector(application, component);

        ConnectorThresholdsResponse response = null;
        try {
            response = connector.getThresholds();
        } catch (IOException e) {
            log.info("Cannot retrieve thresholds for "+application.getName()+", "+component.getName(), e);
            return null;
        }
        return response;
    }


    /**
     * Provides accumulators names data (list of accumulators names).
     *
     * @param application {@link Application}
     * @param component {@link Component}
     *
     * @return {@link ConnectorAccumulatorsNamesResponse}
     */
    public ConnectorAccumulatorsNamesResponse provideAccumulatorsNames(Application application, Component component) {
        Connector connector = getConfiguredConnector(application, component);

        ConnectorAccumulatorsNamesResponse response = null;
        try {
            response = connector.getAccumulatorsNames();
        } catch (IOException e) {
            log.info("Cannot retrieve accumulators list for "+application.getName()+", "+component.getName(), e);
            return null;
        }
        return response;
    }

    /**
     * Provides accumulators charts data.
     *
     * @param application {@link Application}
     * @param component {@link Component}
     * @param accumulatorsNames list of accumulators names to get charts for
     *
     * @return {@link ConnectorAccumulatorResponse}
     */
    public ConnectorAccumulatorResponse provideAccumulatorsCharts(Application application, Component component, List<String> accumulatorsNames) {
        Connector connector = getConfiguredConnector(application, component);

        ConnectorAccumulatorResponse response = null;
        response = connector.getAccumulators(accumulatorsNames);
        return response;
    }

    /**
     * Configures connector for given application and component.
     *
     * @param application {@link Application}
     * @param component {@link Component}
     *
     * @return configured {@link Connector}
     */
    private Connector getConfiguredConnector(Application application, Component component) {
        ComponentConfig componentConfig = MoskitoControlConfiguration.getConfiguration().getApplication(application.getName()).getComponent(component.getName());
        Connector connector = ConnectorFactory.createConnector(componentConfig.getConnectorType());
        connector.configure(componentConfig.getLocation());
        return connector;
    }

}
