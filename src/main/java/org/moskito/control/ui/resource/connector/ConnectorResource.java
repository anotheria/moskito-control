package org.moskito.control.ui.resource.connector;

import org.moskito.control.connectors.response.ConnectorConfigurationResponse;
import org.moskito.control.connectors.response.ConnectorInformationResponse;
import org.moskito.control.core.Application;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.Component;
import org.moskito.control.core.inspection.ComponentInspectionDataProvider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * REST resource used to retrieve connector
 * for specific application and component.
 *
 * @author strel
 */
@Path("/connectors")
@Produces(MediaType.APPLICATION_JSON)
public class ConnectorResource {

	@GET
	@Path("/configuration/{application}/{component}")
	public ConnectorConfigurationRestResponse connectorConfiguration(@PathParam("application") String applicationName, @PathParam("component") String componentName){

		Application application = ApplicationRepository.getInstance().getApplication(applicationName);
		Component component = application.getComponent(componentName);

		ComponentInspectionDataProvider provider = new ComponentInspectionDataProvider();
		ConnectorConfigurationResponse response = provider.provideConnectorConfiguration(application, component);

		ConnectorConfigurationBean bean = new ConnectorConfigurationBean();
		bean.setSupportsAccumulators(response.isSupportsAccumulators());
		bean.setSupportsThresholds(response.isSupportsThresholds());
		bean.setSupportsInfo(response.isSupportsInfo());

		ConnectorConfigurationRestResponse connectorResponse = new ConnectorConfigurationRestResponse();
		connectorResponse.setConnectorConfiguration(bean);

		return connectorResponse;
	}

	@GET
	@Path("/information/{application}/{component}")
	public ConnectorInformationRestResponse connectorInformation(@PathParam("application") String applicationName, @PathParam("component") String componentName){

		Application application = ApplicationRepository.getInstance().getApplication(applicationName);
		Component component = application.getComponent(componentName);

		ComponentInspectionDataProvider provider = new ComponentInspectionDataProvider();
		ConnectorInformationResponse response = provider.provideConnectorInformation(application, component);

		ConnectorInformationBean bean = new ConnectorInformationBean();
		Map<String, String> informationMap = new HashMap<>();

		for (Map.Entry<String, String> property : response.getInfo().entrySet()) {
			informationMap.put(property.getKey(), String.valueOf(property.getValue()));
		}

		bean.setInfo(informationMap);

		ConnectorInformationRestResponse connectorResponse = new ConnectorInformationRestResponse();
		connectorResponse.setConnectorInformation(bean);

		return connectorResponse;
	}
}
