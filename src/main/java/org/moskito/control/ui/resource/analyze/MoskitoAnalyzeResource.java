package org.moskito.control.ui.resource.analyze;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.lang.ArrayUtils;
import org.moskito.control.config.MoskitoAnalyzeChartConfig;
import org.moskito.control.config.MoskitoAnalyzeChartLineConfig;
import org.moskito.control.config.MoskitoAnalyzeConfig;
import org.moskito.control.ui.resource.ControlReplyObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * REST resource used for getting configured MoSKito-Analyze properties.
 * @author strel
 */
@Path("/analyze")
@Produces(MediaType.APPLICATION_JSON)
public class MoskitoAnalyzeResource {

    /**
     * @return General MoSKito-Analyze configuration.
     */
    @GET
    @Path("/configuration")
    public MoskitoAnalyzeConfigResponse getMoskitoAnalyzeConfig() {
        MoskitoAnalyzeConfig analyzeConfig = MoskitoAnalyzeConfig.getInstance();

        // Filling request
        MoskitoAnalyzeConfigResponse response = new MoskitoAnalyzeConfigResponse();
        response.setUrl( analyzeConfig.getUrl() );
        response.setComponents( analyzeConfig.getComponents() );

        return response;
    }

    @GET
    @Path("/configuration/pretty")
    public String getPrettyMoskitoAnalyzeConfig() {
        MoskitoAnalyzeConfig config = MoskitoAnalyzeConfig.getInstance();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(config);

        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(jsonOutput);

        return gson.toJson(je);
    }

    /**
     * @return {@link List} of configured {@link MoskitoAnalyzeChartBean} properties.
     */
    @GET
    @Path("/charts")
    public MoskitoAnalyzeChartsResponse getMoskitoAnalyzeCharts() {
        MoskitoAnalyzeConfig analyzeConfig = MoskitoAnalyzeConfig.getInstance();

        MoskitoAnalyzeChartConfig[] chartsConfig = analyzeConfig.getCharts();
        List<MoskitoAnalyzeChartBean> chartBeans = new ArrayList<>( chartsConfig.length );

        // Copying Moskito-Analyze charts config to chart beans for request
        for (MoskitoAnalyzeChartConfig config : chartsConfig) {
            MoskitoAnalyzeChartBean bean = new MoskitoAnalyzeChartBean();

            bean.setId(config.getId());
            bean.setName(config.getName());
            bean.setCaption(config.getCaption());
            bean.setInterval(config.getInterval());

            List<MoskitoAnalyzeChartLineBean> lineBeans = new ArrayList<>( config.getLines().length );

            for (MoskitoAnalyzeChartLineConfig lineConfig : config.getLines()) {
                MoskitoAnalyzeChartLineBean lineBean = new MoskitoAnalyzeChartLineBean();

                lineBean.setName(lineConfig.getName());
                lineBean.setProducer(lineConfig.getProducer());
                lineBean.setStat(lineConfig.getStat());
                lineBean.setValue(lineConfig.getValue());
                lineBean.setComponents(lineConfig.getComponents());
                lineBean.setAverage(lineConfig.isAverage());
                lineBean.setBaseline(lineConfig.isBaseline());

                lineBeans.add(lineBean);
            }

            bean.setLines(lineBeans);

            bean.setStartDate(config.getStartDate());
            bean.setEndDate(config.getEndDate());

            chartBeans.add(bean);
        }

        // Filling request
        MoskitoAnalyzeChartsResponse response = new MoskitoAnalyzeChartsResponse();
        response.setCharts(chartBeans);

        return response;
    }

    @POST
    @Path("/charts")
    @Consumes("application/json")
    public ControlReplyObject createMoskitoAnalyzeChart(final MoskitoAnalyzeChartBean chartBean) {
        MoskitoAnalyzeConfig analyzeConfig = MoskitoAnalyzeConfig.getInstance();

        MoskitoAnalyzeChartConfig chartConfig = new MoskitoAnalyzeChartConfig();
        chartConfig.setName(chartBean.getName());
        chartConfig.setCaption(chartBean.getCaption());
        chartConfig.setInterval(chartBean.getInterval());

        List<MoskitoAnalyzeChartLineConfig> lineConfigs = new ArrayList<>( chartBean.getLines().size() );

        for (MoskitoAnalyzeChartLineBean lineBean : chartBean.getLines()) {
            MoskitoAnalyzeChartLineConfig lineConfig = new MoskitoAnalyzeChartLineConfig();

            lineConfig.setName(lineBean.getName());
            lineConfig.setProducer(lineBean.getProducer());
            lineConfig.setStat(lineBean.getStat());
            lineConfig.setValue(lineBean.getValue());
            lineConfig.setComponents(lineBean.getComponents());
            lineConfig.setAverage(lineBean.isAverage());
            lineConfig.setBaseline(lineBean.isBaseline());

            lineConfigs.add(lineConfig);
        }

        chartConfig.setLines(lineConfigs.toArray(new MoskitoAnalyzeChartLineConfig[lineConfigs.size()]));

        chartConfig.setStartDate(chartBean.getStartDate());
        chartConfig.setEndDate(chartBean.getEndDate());

        MoskitoAnalyzeChartConfig[] chartConfigs = analyzeConfig.getCharts();
        analyzeConfig.setCharts((MoskitoAnalyzeChartConfig[]) ArrayUtils.add(chartConfigs, chartConfig));

        return new ControlReplyObject();
    }

    @PUT
    @Path("/charts/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public ControlReplyObject updateMoskitoAnalyzeChart(@PathParam("id") String id, final MoskitoAnalyzeChartBean chartBean) {
        MoskitoAnalyzeConfig analyzeConfig = MoskitoAnalyzeConfig.getInstance();

        MoskitoAnalyzeChartConfig chartConfig = new MoskitoAnalyzeChartConfig();
        chartConfig.setName(chartBean.getName());
        chartConfig.setCaption(chartBean.getCaption());
        chartConfig.setInterval(chartBean.getInterval());

        List<MoskitoAnalyzeChartLineConfig> lineConfigs = new ArrayList<>( chartBean.getLines().size() );

        for (MoskitoAnalyzeChartLineBean lineBean : chartBean.getLines()) {
            MoskitoAnalyzeChartLineConfig lineConfig = new MoskitoAnalyzeChartLineConfig();

            lineConfig.setName(lineBean.getName());
            lineConfig.setProducer(lineBean.getProducer());
            lineConfig.setStat(lineBean.getStat());
            lineConfig.setValue(lineBean.getValue());
            lineConfig.setComponents(lineBean.getComponents());
            lineConfig.setAverage(lineBean.isAverage());
            lineConfig.setBaseline(lineBean.isBaseline());

            lineConfigs.add(lineConfig);
        }

        chartConfig.setLines(lineConfigs.toArray(new MoskitoAnalyzeChartLineConfig[lineConfigs.size()]));

        chartConfig.setStartDate(chartBean.getStartDate());
        chartConfig.setEndDate(chartBean.getEndDate());

        analyzeConfig.updateChartById(id, chartConfig);

        return new ControlReplyObject();
    }

    @DELETE
    @Path("/charts/{id}")
    @Produces("application/json")
    public ControlReplyObject removeMoskitoAnalyzeChart(@PathParam("id") String id) {
        MoskitoAnalyzeConfig analyzeConfig = MoskitoAnalyzeConfig.getInstance();
        analyzeConfig.removeChartById(id);
        return new ControlReplyObject();
    }
}
