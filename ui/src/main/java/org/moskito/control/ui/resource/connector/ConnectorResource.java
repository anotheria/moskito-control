package org.moskito.control.ui.resource.connector;

import io.swagger.v3.oas.annotations.servers.Server;
import org.moskito.control.connectors.response.ConnectorInspectionDataSupportResponse;
import org.moskito.control.connectors.response.ConnectorInformationResponse;
import org.moskito.control.core.Component;
import org.moskito.control.core.Repository;
import org.moskito.control.core.inspection.ComponentInspectionDataProvider;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * REST resource used to retrieve connector
 * for specific application and component.
 *
 * @author strel
 */
@Path("/connectors")
@Produces(MediaType.APPLICATION_JSON)
@Server(url = "/rest")
public class ConnectorResource {

	@GET
	@Path("/configuration/{component}")
	public ConnectorConfigurationRestResponse connectorConfiguration( @PathParam("component") String componentName){

		Component component = Repository.getInstance().getComponent(componentName);

		ComponentInspectionDataProvider provider = new ComponentInspectionDataProvider();
		ConnectorInspectionDataSupportResponse response = provider.provideConnectorInspectionDataSupport(component);

		ConnectorConfigurationBean bean = new ConnectorConfigurationBean();
		bean.setSupportsAccumulators(response.isSupportsAccumulators());
		bean.setSupportsThresholds(response.isSupportsThresholds());
		bean.setSupportsInfo(response.isSupportsInfo());

		ConnectorConfigurationRestResponse connectorResponse = new ConnectorConfigurationRestResponse();
		connectorResponse.setConnectorConfiguration(bean);

		return connectorResponse;
	}

	@GET
	@Path("/information/{component}")
	public ConnectorInformationRestResponse connectorInformation( @PathParam("component") String componentName){

		Component component = Repository.getInstance().getComponent(componentName);

		ComponentInspectionDataProvider provider = new ComponentInspectionDataProvider();
		ConnectorInformationResponse response = provider.provideConnectorInformation(component);

		ConnectorInformationBean bean = new ConnectorInformationBean();
		bean.setInfo(response.getInfo());

		ConnectorInformationRestResponse connectorResponse = new ConnectorInformationRestResponse();
		connectorResponse.setConnectorInformation(bean);

		return connectorResponse;
	}
}
