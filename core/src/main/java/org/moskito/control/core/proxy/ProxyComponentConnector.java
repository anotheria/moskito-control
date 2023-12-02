package org.moskito.control.core.proxy;

import org.moskito.control.config.ComponentConfig;
import org.moskito.control.config.ProxyConfig;
import org.moskito.control.connectors.AbstractConnector;
import org.moskito.control.connectors.Connector;
import org.moskito.control.connectors.response.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * ProxyComponentConnector is used to retrieve component information from a different MoSKito Control instance.
 * It only works in cooperation with a ProxiedConnector, meaning that the ProxiedConnector is responsible for providing the view data, and
 * this connector serves for component inspection to retrieve thresholds and accumulators data.
 * Its convenient to have this connector implement Connector interface, so nothing in the rendering code has to be changed.
 */
public class ProxyComponentConnector extends AbstractConnector implements Connector {

    private ProxyConfig config;

    public ProxyComponentConnector(ProxyConfig proxyConfig){
        this.config = proxyConfig;

    }

    @Override
    public void configure(ComponentConfig connectorConfig) {
        throw  new UnsupportedOperationException("This connector is not supposed to be used for configuration");
    }

    @Override
    public ConnectorStatusResponse getNewStatus() {
        throw new UnsupportedOperationException("This connector is not supposed to be used for status retrieval");
    }

    @Override
    public ConnectorThresholdsResponse getThresholds() {
        System.out.println("THRESHOLDS requested for ");
        return new ConnectorThresholdsResponse();
    }

    @Override
    public ConnectorAccumulatorResponse getAccumulators(List<String> accumulatorNames) {
        System.out.println("Accumulators requested for ");
        return null;
    }

    @Override
    public ConnectorAccumulatorsNamesResponse getAccumulatorsNames() throws IOException {
        System.out.println("getAccumulatorsNames requested for ");

        return new ConnectorAccumulatorsNamesResponse(Collections.emptyList());
    }

    @Override
    public ConnectorInformationResponse getInfo() {
        System.out.println("Accumulator names requested for");
        ConnectorInformationResponse response = new ConnectorInformationResponse();
        return response;
    }
}
