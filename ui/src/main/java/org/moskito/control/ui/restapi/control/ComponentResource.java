package org.moskito.control.ui.restapi.control;

import io.swagger.v3.oas.annotations.servers.Server;
import net.anotheria.util.NumberUtils;
import org.moskito.control.common.AccumulatorDataItem;
import org.moskito.control.common.ThresholdDataItem;
import org.moskito.control.connectors.Connector;
import org.moskito.control.connectors.ConnectorFactory;
import org.moskito.control.connectors.response.ConnectorAccumulatorResponse;
import org.moskito.control.connectors.response.ConnectorAccumulatorsNamesResponse;
import org.moskito.control.connectors.response.ConnectorConfigResponse;
import org.moskito.control.connectors.response.ConnectorThresholdsResponse;
import org.moskito.control.core.Component;
import org.moskito.control.core.Repository;
import org.moskito.control.core.chart.Chart;
import org.moskito.control.core.inspection.ComponentInspectionDataProvider;
import org.moskito.control.ui.action.MainViewAction;
import org.moskito.control.ui.restapi.ReplyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Path("component")
@Produces(MediaType.APPLICATION_JSON)
@Server(url = "/api/v2")
/**
 * This class is responsible for handling methods for component inspection.
 */
public class ComponentResource {

    private static Logger log = LoggerFactory.getLogger(ComponentResource.class);

    @GET
    @Path("{componentName}/thresholds")
    public ReplyObject getThresholds(@PathParam("componentName") String componentName) {

        Component component = Repository.getInstance().getComponent(componentName);

        ComponentInspectionDataProvider provider = new ComponentInspectionDataProvider();
        ConnectorThresholdsResponse response = provider.provideThresholds(component);

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

        return ReplyObject.success("thresholds", thresholdBeans);
    }


    @GET
    @Path("{componentName}/accumulators")
    public ReplyObject getAccumulators(@PathParam("componentName") String componentName) {
        Component component = Repository.getInstance().getComponent(componentName);

        ComponentInspectionDataProvider provider = new ComponentInspectionDataProvider();
        ConnectorAccumulatorsNamesResponse response = provider.provideAccumulatorsNames(component);

        Collections.sort(response.getNames());

        return ReplyObject.success("accumulators", response.getNames());
    }

    @GET
    @Path("{componentName}/info")
    public ReplyObject getConnectorInfo(@PathParam("componentName") String componentName) {
        Component component = Repository.getInstance().getComponent(componentName);
        Connector connector = ConnectorFactory.createConnector(component.getConfiguration().getConnectorType());
        connector.configure(component.getConfiguration());

        ReplyObject ret = ReplyObject.success();
        Map<String, String> info = connector.getInfo().getInfo();
        for (Map.Entry<String, String> entry : info.entrySet()) {
            ret.addResult(entry.getKey(), entry.getValue());
        }
        return ret;
    }

    @GET
    @Path("{componentName}/config")
    public ReplyObject getComponentConfig(@PathParam("componentName") String componentName) {
        Component component = Repository.getInstance().getComponent(componentName);

        ComponentInspectionDataProvider provider = new ComponentInspectionDataProvider();
        ConnectorConfigResponse configResponse = provider.provideConfig(component);

        String config = configResponse.getConfig();

        ReplyObject ret = ReplyObject.success();
        ret.addResult("config", config);
        return ret;
    }


    @GET @Path("{componentName}/capabilities")
    public ReplyObject getComponentCapabilities(@PathParam("componentName") String componentName){
        try {
            Component component = Repository.getInstance().getComponent(componentName);
            Connector connector = ConnectorFactory.createConnector(component.getConfiguration().getConnectorType());
            connector.configure(component.getConfiguration());
            ReplyObject ret = ReplyObject.success();
            ret.addResult("threshold", connector.supportsThresholds());
            ret.addResult("accumulators", connector.supportsAccumulators());
            ret.addResult("config", connector.supportsConfig());
            ret.addResult("nowRunning", connector.supportsNowRunning());
            ret.addResult("connectorInfo", connector.supportsInfo());
            ret.addResult("componentInfo", "true");
            ret.addResult("history", "true");
            ret.addResult("actions", "true");
            return ret;
        }catch(Exception any){
            any.printStackTrace();
            log.error("getComponentCapabilities("+componentName+")", any);
            return ReplyObject.error(any);
        }
    }

    @POST
    @Path("/charts")
    @Consumes(MediaType.APPLICATION_JSON)
    public ReplyObject accumulatorCharts(AccumulatorChartsParameters params) {
        Component component = Repository.getInstance().getComponent(params.getComponent());
        ArrayList<String> accumulators = params.getAccumulators();

        if (accumulators == null || accumulators.isEmpty()) {
            return ReplyObject.success();
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

        ReplyObject response = ReplyObject.success();
        response.addResult("charts", MainViewAction.prepareChartData(chartBeans));
        return response;
    }

}