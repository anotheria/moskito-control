package org.moskito.control.ui.resource.thresholds;

import net.anotheria.util.NumberUtils;
import org.moskito.control.connectors.response.ConnectorThresholdsResponse;
import org.moskito.control.core.Application;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.Component;
import org.moskito.control.core.inspection.ComponentInspectionDataProvider;
import org.moskito.control.core.threshold.ThresholdDataItem;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;

/**
 * REST resource used to retrieve list of component thresholds.
 * @author strel
 */
@Path("/thresholds")
@Produces(MediaType.APPLICATION_JSON)
public class ThresholdResource {

	@GET
	@Path("/{application}/{component}")
	public ThresholdsListResponse componentThresholds(@PathParam("application") String applicationName, @PathParam("component") String componentName){

		Application application = ApplicationRepository.getInstance().getApplication(applicationName);
		Component component = application.getComponent(componentName);

		ComponentInspectionDataProvider provider = new ComponentInspectionDataProvider();
		ConnectorThresholdsResponse response = provider.provideThresholds(application, component);

		LinkedList<ThresholdBean> thresholdBeans = new LinkedList<>();
		List<ThresholdDataItem> items = response.getItems();
		for (ThresholdDataItem item : items) {
			ThresholdBean threshold = new ThresholdBean();
			threshold.setName(item.getName());
			threshold.setStatus(item.getStatus().toString().toLowerCase());
			threshold.setLastValue(item.getLastValue());
			threshold.setStatusChangeTimestamp(NumberUtils.makeISO8601TimestampString(item.getStatusChangeTimestamp()));

			thresholdBeans.add(threshold);
		}

		ThresholdsListResponse thresholds = new ThresholdsListResponse();
		thresholds.setThresholds(thresholdBeans);

		return thresholds;
	}
}
