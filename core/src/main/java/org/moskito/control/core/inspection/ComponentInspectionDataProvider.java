package org.moskito.control.core.inspection;

import org.moskito.control.config.ComponentConfig;
import org.moskito.control.connectors.Connector;
import org.moskito.control.connectors.ConnectorException;
import org.moskito.control.connectors.ConnectorFactory;
import org.moskito.control.connectors.response.ConnectorAccumulatorResponse;
import org.moskito.control.connectors.response.ConnectorAccumulatorsNamesResponse;
import org.moskito.control.connectors.response.ConnectorConfigResponse;
import org.moskito.control.connectors.response.ConnectorInformationResponse;
import org.moskito.control.connectors.response.ConnectorInspectionDataSupportResponse;
import org.moskito.control.connectors.response.ConnectorStatusResponse;
import org.moskito.control.connectors.response.ConnectorThresholdsResponse;
import org.moskito.control.core.Component;
import org.moskito.control.core.ComponentRepository;
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
     * @param component {@link Component}
     * @return {@link ConnectorThresholdsResponse}
     */
    public ConnectorThresholdsResponse provideThresholds(Component component) {
        Connector connector = getConfiguredConnector(component);

        ConnectorThresholdsResponse response = null;
        try {
            response = connector.getThresholds();
            ConnectorStatusResponse newStatus = connector.getNewStatus();
            component.setStatus(newStatus.getStatus());
        } catch (ConnectorException e) {
            log.info("Cannot retrieve thresholds for " + component.getName(), e);
            return null;
        }
        return response;
    }


    /**
     * Provides accumulators names data (list of accumulators names).
     *
     * @param component {@link Component}
     * @return {@link ConnectorAccumulatorsNamesResponse}
     */
    public ConnectorAccumulatorsNamesResponse provideAccumulatorsNames(Component component) {
        Connector connector = getConfiguredConnector(component);

        ConnectorAccumulatorsNamesResponse response = null;
        try {
            response = connector.getAccumulatorsNames();
        } catch (IOException e) {
            log.info("Cannot retrieve accumulators list for " + component.getName(), e);
            return null;
        }
        return response;
    }

    /**
     * Provides accumulators charts data.
     *
     * @param component         {@link Component}
     * @param accumulatorsNames list of accumulators names to get charts for
     * @return {@link ConnectorAccumulatorResponse}
     */
    public ConnectorAccumulatorResponse provideAccumulatorsCharts(Component component, List<String> accumulatorsNames) {
        Connector connector = getConfiguredConnector(component);

        ConnectorAccumulatorResponse response = null;
        response = connector.getAccumulators(accumulatorsNames);
        return response;
    }

    /**
     * Provides connector information data.
     *
     * @param component {@link Component}
     * @return {@link ConnectorAccumulatorResponse}
     */
    public ConnectorInformationResponse provideConnectorInformation(Component component) {
        return getConfiguredConnector(component).getInfo();
    }

    /**
     * Provides supported inspection data for given component.
     *
     * @param component {@link Component}
     * @return {@link ConnectorInspectionDataSupportResponse}
     */
    public ConnectorInspectionDataSupportResponse provideConnectorInspectionDataSupport(Component component) {
        Connector connector = getConfiguredConnector(component);

        ConnectorInspectionDataSupportResponse dataSupportResponse = new ConnectorInspectionDataSupportResponse();
        dataSupportResponse.setSupportsAccumulators(connector.supportsAccumulators());
        dataSupportResponse.setSupportsThresholds(connector.supportsThresholds());
        dataSupportResponse.setSupportsInfo(connector.supportsInfo());
        dataSupportResponse.setSupportsConfig(connector.supportsConfig());

        return dataSupportResponse;
    }

    /**
     * Provides component's config.
     *
     * @param component {@link Component}
     * @return {@link ConnectorConfigResponse}
     */
    public ConnectorConfigResponse provideConfig(Component component) {
        return getConfiguredConnector(component).getConfig();
    }

    /**
     * Configures connector for given application and component.
     *
     * @param component {@link Component}
     * @return configured {@link Connector}
     */
    private Connector getConfiguredConnector(Component component) {
        ComponentConfig componentConfig = ComponentRepository.getInstance().getComponent(component.getName()).getConfiguration();
        Connector connector = ConnectorFactory.createConnector(componentConfig.getConnectorType());
        connector.configure(componentConfig.getName(), componentConfig.getLocation(), componentConfig.getCredentials());
        return connector;
    }

}
