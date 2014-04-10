package org.moskito.control.connectors;

import net.anotheria.util.StringUtils;
import org.distributeme.core.ServiceDescriptor;
import org.moskito.control.connectors.response.ConnectorAccumulatorResponse;
import org.moskito.control.connectors.response.ConnectorAccumulatorsNamesResponse;
import org.moskito.control.connectors.response.ConnectorStatusResponse;
import org.moskito.control.connectors.response.ConnectorThresholdsResponse;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.status.Status;
import org.moskito.controlagent.data.status.ThresholdInfo;
import org.moskito.controlagent.data.status.ThresholdStatusHolder;
import org.moskito.controlagent.endpoints.rmi.AgentService;
import org.moskito.controlagent.endpoints.rmi.AgentServiceException;
import org.moskito.controlagent.endpoints.rmi.generated.AgentServiceConstants;
import org.moskito.controlagent.endpoints.rmi.generated.RemoteAgentServiceStub;

import java.io.IOException;
import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 10.04.14 08:49
 */
public class RMIConnector implements Connector {

	private AgentService theOtherSideEndpoint;

	@Override
	public void configure(String location) {
		String tokens[] = StringUtils.tokenize(location, ':');
		if (tokens.length!=2)
			throw new IllegalArgumentException("Location should be formed as host:port, misconfiguration in location: "+location);
		String host = tokens[0];
		int port = Integer.parseInt(tokens[1]);
		ServiceDescriptor descriptor = new ServiceDescriptor(ServiceDescriptor.Protocol.RMI, AgentServiceConstants.getServiceId(), "any", host, port);
		theOtherSideEndpoint = new RemoteAgentServiceStub(descriptor);
	}

	@Override
	public ConnectorStatusResponse getNewStatus() {
		HealthColor color = null;
		Status status = new Status();
		try{
			ThresholdStatusHolder holder = theOtherSideEndpoint.getThresholdStatus();
			color = HealthColor.getHealthColor(holder.getStatus());
			for (ThresholdInfo info : holder.getThresholds())
				status.addMessage(info.toString());
		}catch(AgentServiceException e){
			color = HealthColor.PURPLE;
			status.addMessage("Can't connect due to "+e.getMessage());
		}
		status.setHealth(color);
		ConnectorStatusResponse statusResponse = new ConnectorStatusResponse(status);
		return statusResponse;
	}

	@Override
	public ConnectorThresholdsResponse getThresholds() throws IOException {
		return null;
	}

	@Override
	public ConnectorAccumulatorResponse getAccumulators(List<String> accumulatorNames) {
		return null;
	}

	@Override
	public ConnectorAccumulatorsNamesResponse getAccumulatorsNames() throws IOException {
		return null;
	}
}
