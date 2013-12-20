package org.moskito.control.core.provider;

import org.moskito.control.config.ComponentConfig;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.connectors.Connector;
import org.moskito.control.connectors.ConnectorFactory;
import org.moskito.control.connectors.response.ConnectorAccumulatorsNamesResponse;
import org.moskito.control.core.Application;
import org.moskito.control.core.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Provides accumulators names data (list of accumulators names).
 *
 * @see DataProvider
 *
 * @author Vladyslav Bezuhlyi
 */
public class AccumulatorsNamesDataProvider implements DataProvider {

    /**
     * Logger.
     */
    Logger log = LoggerFactory.getLogger(AccumulatorsNamesDataProvider.class);


    /**
     * {@inheritDoc}
     */
    @Override
    public ConnectorAccumulatorsNamesResponse provideData(Application application, Component component) {
        ComponentConfig componentConfig = MoskitoControlConfiguration.getConfiguration().getApplication(application.getName()).getComponent(component.getName());
        Connector connector = ConnectorFactory.createConnector(componentConfig.getConnectorType());
        connector.configure(componentConfig.getLocation());

        ConnectorAccumulatorsNamesResponse response = null;
        try {
            response = connector.getAccumulatorsNames();
        } catch (IOException e) {
            log.info("Cannot retrieve accumulators list for "+application.getName()+", "+component.getName(), e);
            return null;
        }
        return response;
    }

}
