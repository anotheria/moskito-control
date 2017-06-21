package org.moskito.control.ui.resource.connector;

import org.moskito.control.connectors.response.ConnectorInfoResponse;
import org.moskito.control.core.Application;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.Component;
import org.moskito.control.core.inspection.ComponentInspectionDataProvider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
	@Path("/{application}/{component}")
	public ConnectorRestResponse componentConnector(@PathParam("application") String applicationName, @PathParam("component") String componentName){

		Application application = ApplicationRepository.getInstance().getApplication(applicationName);
		Component component = application.getComponent(componentName);

		ComponentInspectionDataProvider provider = new ComponentInspectionDataProvider();
		ConnectorInfoResponse response = provider.provideConnectorInfo(application, component);

		ConnectorBean bean = new ConnectorBean();
		bean.setSupportsAccumulators(response.isSupportsAccumulators());
		bean.setSupportsThresholds(response.isSupportsThresholds());
		bean.setSupportsInfo(response.isSupportsInfo());
		bean.setInfo(response.getInfo());

		ConnectorRestResponse connectorResponse = new ConnectorRestResponse();
		connectorResponse.setConnector(bean);

		return connectorResponse;
	}
}
