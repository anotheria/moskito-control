package org.moskito.control.ui.resource.accumulators;

import org.moskito.control.connectors.response.ConnectorAccumulatorResponse;
import org.moskito.control.connectors.response.ConnectorAccumulatorsNamesResponse;
import org.moskito.control.core.ComponentRepository;
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
	@Path("/{component}")
	public AccumulatorsListBean componentAccumulatorNames(@PathParam("component") String componentName){

		Component component = ComponentRepository.getInstance().getComponent(componentName);

		ComponentInspectionDataProvider provider = new ComponentInspectionDataProvider();
		ConnectorAccumulatorsNamesResponse response = provider.provideAccumulatorsNames(component);

		Collections.sort(response.getNames());

		AccumulatorsListBean componentAccumulators = new AccumulatorsListBean();
		componentAccumulators.setNames(response.getNames());
		componentAccumulators.setComponentName(componentName);

		return componentAccumulators;
	}

	@POST
	@Path("/charts")
	@Consumes(MediaType.APPLICATION_JSON)
	public AccumulatorChartsListResponse accumulatorCharts(AccumulatorChartsParameters params) {
		Component component = ComponentRepository.getInstance().getComponent(params.getComponent());
		ArrayList<String> accumulators = params.getAccumulators();

		if (accumulators == null || accumulators.isEmpty()) {
			return new AccumulatorChartsListResponse();
		}

		ComponentInspectionDataProvider provider = new ComponentInspectionDataProvider();
		ConnectorAccumulatorResponse accumulatorResponse = provider.provideAccumulatorsCharts(component, accumulators);

		LinkedList<Chart> chartBeans = new LinkedList<>();
		Collection<String> names = accumulatorResponse.getNames();
		for (String name : names) {
			List<AccumulatorDataItem> line = accumulatorResponse.getLine(name);
			String accumulatorName = name+"-"+component.getName(); // to avoid same accumulators ids for multiple components

			Chart chart = new Chart(accumulatorName, -1);
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
