package org.moskito.control.core.provider;

import org.moskito.control.connectors.response.ConnectorResponse;
import org.moskito.control.core.Application;
import org.moskito.control.core.Component;

/**
 * Provides the most recent data for separate section of component on-demand.
 *
 * @author Vladyslav Bezuhlyi
 */
public interface DataProvider {

    /**
     * Updates section of component's data.
     *
     * @param application {@link Application}
     * @param component {@link Component}
     */
    ConnectorResponse provideData(Application application, Component component);
}
