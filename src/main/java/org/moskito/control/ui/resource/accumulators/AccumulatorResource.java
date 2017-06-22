package org.moskito.control.ui.resource.accumulators;

import org.moskito.control.connectors.response.ConnectorAccumulatorResponse;
import org.moskito.control.connectors.response.ConnectorAccumulatorsNamesResponse;
import org.moskito.control.core.Application;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.Component;
import org.moskito.control.core.accumulator.AccumulatorDataItem;
import org.moskito.control.core.chart.Chart;
import org.moskito.control.core.inspection.ComponentInspectionDataProvider;
import org.moskito.control.ui.action.MainViewAction;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * REST resource used to list of accumulator chart beans.
 * @author strel
 */
@Path("/accumulators")
@Produces(MediaType.APPLICATION_JSON)
public class AccumulatorResource {

	@GET
	@Path("/{application}/{component}")
	public AccumulatorsListBean componentAccumulatorNames(@PathParam("application") String applicationName, @PathParam("component") String componentName){

		Application application = ApplicationRepository.getInstance().getApplication(applicationName);
		Component component = application.getComponent(componentName);

		ComponentInspectionDataProvider provider = new ComponentInspectionDataProvider();
		ConnectorAccumulatorsNamesResponse response = provider.provideAccumulatorsNames(application, component);

		Collections.sort(response.getNames());

		AccumulatorsListBean componentAccumulators = new AccumulatorsListBean();
		componentAccumulators.setNames(response.getNames());
		componentAccumulators.setApplicationName(applicationName);
		componentAccumulators.setComponentName(componentName);

		return componentAccumulators;
	}

	@POST
	@Path("/charts")
	@Consumes(MediaType.APPLICATION_JSON)
	public AccumulatorChartsListResponse accumulatorCharts(AccumulatorChartsParameters params) {
		Application application = ApplicationRepository.getInstance().getApplication(params.getApplication());
		Component component = application.getComponent(params.getComponent());
		ArrayList<String> accumulators = params.getAccumulators();

		if (accumulators == null || accumulators.isEmpty()) {
			return new AccumulatorChartsListResponse();
		}

		ComponentInspectionDataProvider provider = new ComponentInspectionDataProvider();
		ConnectorAccumulatorResponse accumulatorResponse = provider.provideAccumulatorsCharts(application, component, accumulators);

		LinkedList<Chart> chartBeans = new LinkedList<>();
		Collection<String> names = accumulatorResponse.getNames();
		for (String name : names) {
			List<AccumulatorDataItem> line = accumulatorResponse.getLine(name);
			String accumulatorName = name+"-"+application.getName()+"-"+component.getName(); // to avoid same accumulators ids for multiple components

			Chart chart = new Chart(application, accumulatorName, -1);
			chart.addLine(component.getName(), accumulatorName);
			chart.notifyNewData(component.getName(), accumulatorName, line);

			chartBeans.add(chart);
		}

		Collections.sort(chartBeans, new Comparator<Chart>() {
			@Override
			public int compare(Chart chart, Chart another) {
				return chart.getName().compareTo(another.getName());
			}
		});

		AccumulatorChartsListResponse response = new AccumulatorChartsListResponse();
		response.setCharts(MainViewAction.prepareChartData(chartBeans));

		return response;
	}
}
