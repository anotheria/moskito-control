package org.moskito.control.ui.restapi.control;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.anotheria.util.NumberUtils;
import org.moskito.control.common.AccumulatorDataItem;
import org.moskito.control.common.ThresholdDataItem;
import org.moskito.control.config.ConnectorType;
import org.moskito.control.config.HeaderParameter;
import org.moskito.control.config.HttpMethodType;
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
import org.moskito.control.core.proxy.ProxiedComponent;
import org.moskito.control.ui.action.MainViewAction;
import org.moskito.control.ui.restapi.ReplyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.*;

@Path("component")
@Produces(MediaType.APPLICATION_JSON)
@Server(url = "/api/v2")
@Tag(name = "Component API", description = "API for component inspection")
/**
 * This class is responsible for handling methods for component inspection.
 */
public class ComponentResource {

    private static Logger log = LoggerFactory.getLogger(ComponentResource.class);

    @GET
    @Path("{componentName}/thresholds")
    @Operation(summary = "Returns thresholds in the component",
            description = "Returns all thresholds from the connected component."
    )
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
    @Operation(summary = "Returns accumulators in the component",
       description = "Returns all accumulators/charts from the connected component. Only usable if accumulators are in the capabilities."
    )
    public ReplyObject getAccumulators(@PathParam("componentName") String componentName) {
        Component component = Repository.getInstance().getComponent(componentName);

        ComponentInspectionDataProvider provider = new ComponentInspectionDataProvider();
        ConnectorAccumulatorsNamesResponse response = provider.provideAccumulatorsNames(component);

        Collections.sort(response.getNames());

        return ReplyObject.success("accumulators", response.getNames());
    }

    @GET
    @Path("{componentName}/connectorInfo")
    @Operation(summary = "Returns connector info for this component",
            description = "Connector info is the information that the connector is able to retrieve."
    )
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
    @Path("{componentName}/componentInfo")
    @Operation(summary = "Returns component info for this component",
            description = "Component info is local knowledge about the component, mainly including configuration details."
    )
    public ReplyObject getComponentInfo(@PathParam("componentName") String componentName) {
        Component component = Repository.getInstance().getComponent(componentName);

        ReplyObject ret = ReplyObject.success();

        ret.addResult("Name", component.getName());
        ret.addResult("Category", component.getCategory());
        //Component configuration is null for dynamic components.
        if (component.getConfiguration() != null) {
            ret.addResult("Location", component.getConfiguration().getLocation());
            ret.addResult("Connector type", component.getConfiguration().getConnectorType().name());
        }
        ret.addResult("Tags", "" + component.getTags());
        ret.addResult("Last Update ts", "" + component.getLastUpdateTimestamp());
        ret.addResult("Last Update", NumberUtils.makeISO8601TimestampString(component.getLastUpdateTimestamp()));
        ret.addResult("Update age", "" + ((System.currentTimeMillis() - component.getLastUpdateTimestamp()) / 1000) + " sec");
        ret.addResult("Update type", component.isDynamic() ? "push" : "pull");

        if (component instanceof ProxiedComponent) {
            ret.addResult("Origin Name", ((ProxiedComponent) component).getOriginName());
            ret.addResult("Proxy Config", ((ProxiedComponent) component).getConfig().toString());
        }

        if (component.getConfiguration()!=null && component.getConfiguration().getConnectorType() == ConnectorType.URL) {
            String method = component.getConfiguration().getData().get("methodType");
            HttpMethodType methodType = method == null ? null : HttpMethodType.valueOf(method);
            if (methodType != null) {
                ret.addResult("Method Type", methodType.name());
            }

            String payload = component.getConfiguration().getData().get("payload");
            if (payload != null) {
                ret.addResult("Payload", payload);
            }

            String contentType = component.getConfiguration().getData().get("contentType");
            if (contentType != null) {
                ret.addResult("Content-Type", contentType);
            }

            if (component.getConfiguration().getHeaders() != null) {
                ret.addResult("Headers", formatHeaders(component.getConfiguration().getHeaders()));
            }
        }


        Map<String, String> attributes = component.getAttributes();
        if (attributes != null && attributes.size() > 0)
            ret.addResult("attributes", attributes);

        return ret;
    }


    @GET
    @Path("{componentName}/config")
    @Operation(summary = "Returns component's config",
            description = "Returns component's config. This is the internal configuration of the remote component.")
    public ReplyObject getComponentConfig(@PathParam("componentName") String componentName) {
        Component component = Repository.getInstance().getComponent(componentName);

        ComponentInspectionDataProvider provider = new ComponentInspectionDataProvider();
        ConnectorConfigResponse configResponse = provider.provideConfig(component);

        String config = configResponse.getConfig();

        ReplyObject ret = ReplyObject.success();
        ret.addResult("config", config);
        return ret;
    }


    @Operation(summary = "Returns capabilities of this component",
            description = "Returns all capabilities. This includes threshold, accumulators, config, nowRunning, connectorInfo, componentInfo, history and actions ."
    )
    @GET @Path("{componentName}/capabilities")
    public ReplyObject getComponentCapabilities(@PathParam("componentName") String componentName){
        try {
            Component component = Repository.getInstance().getComponent(componentName);
            Connector connector = ConnectorFactory.createConnector(component.getConfiguration().getConnectorType());
            connector.configure(component.getConfiguration());
            ReplyObject ret = ReplyObject.success();
            ret.addResult("thresholds", connector.supportsThresholds());
            ret.addResult("accumulators", connector.supportsAccumulators());
            ret.addResult("config", connector.supportsConfig());
            ret.addResult("nowRunning", connector.supportsNowRunning());
            ret.addResult("connectorInfo", connector.supportsInfo());
            ret.addResult("componentInfo", Boolean.TRUE);
            ret.addResult("history", Boolean.TRUE);
            ret.addResult("actions", Boolean.TRUE);
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

    private String formatHeaders(HeaderParameter[] headers) {
        if (headers == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        int counter = 1;
        for (HeaderParameter header : headers) {
            result.append(header.getKey()).append("<b>:</b>");
            if (header.getValue().length() > 5) {
                result.append(header.getValue(), 0, 5).append("...");
            } else {
                result.append(header.getValue());
            }
            if (counter != headers.length) {
                result.append(";");
            }
            counter++;
        }
        return result.toString();
    }

}
