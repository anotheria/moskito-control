package org.moskito.control.ui.restapi.control;

import io.swagger.v3.oas.annotations.servers.Server;
import net.anotheria.util.NumberUtils;
import org.moskito.control.common.ThresholdDataItem;
import org.moskito.control.connectors.Connector;
import org.moskito.control.connectors.ConnectorFactory;
import org.moskito.control.connectors.response.ConnectorAccumulatorsNamesResponse;
import org.moskito.control.connectors.response.ConnectorConfigResponse;
import org.moskito.control.connectors.response.ConnectorThresholdsResponse;
import org.moskito.control.core.Component;
import org.moskito.control.core.Repository;
import org.moskito.control.core.inspection.ComponentInspectionDataProvider;
import org.moskito.control.ui.restapi.ReplyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
            ret.addResult("info", connector.supportsInfo());
            return ret;
        }catch(Exception any){
            any.printStackTrace();
            log.error("getComponentCapabilities("+componentName+")", any);
            return ReplyObject.error(any);
        }

    }
}