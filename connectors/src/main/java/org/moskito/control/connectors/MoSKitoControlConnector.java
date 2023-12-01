package org.moskito.control.connectors;

import org.moskito.control.common.HealthColor;
import org.moskito.control.common.Status;
import org.moskito.control.config.ComponentConfig;
import org.moskito.control.connectors.response.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MoSKitoControlConnector extends AbstractConnector implements Connector {

    private Random random = new Random(System.currentTimeMillis());

    @Override
    public void configure(ComponentConfig connectorConfig) {
        System.out.println("Configuring MoSKitoControlConnector" +connectorConfig);
    }

    @Override
    public ConnectorStatusResponse getNewStatus() {
        HealthColor colour = HealthColor.values()[random.nextInt(HealthColor.values().length-1)];
        String message = null;
        if (colour != HealthColor.GREEN) {
            message = "MoSKitoControlConnector message "+colour+" "+random.nextInt(1000);
        }
        return new ConnectorStatusResponse(new Status(colour, message  ));
    }

    @Override
    public ConnectorThresholdsResponse getThresholds() {
        return new ConnectorThresholdsResponse();
    }

    @Override
    public ConnectorAccumulatorResponse getAccumulators(List<String> accumulatorNames) {
        return null;
    }

    @Override
    public ConnectorAccumulatorsNamesResponse getAccumulatorsNames() throws IOException {
        return new ConnectorAccumulatorsNamesResponse(Collections.emptyList());
    }

    @Override
    public ConnectorInformationResponse getInfo() {
        ConnectorInformationResponse response = new ConnectorInformationResponse();
        return response;
    }
}
