package org.moskito.control.ui.resource.analyze;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.moskito.control.config.MoskitoAnalyzeChartConfig;
import org.moskito.control.config.MoskitoAnalyzeConfig;
import org.moskito.control.config.ProducerConfig;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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

        return response;
    }

    @GET
    @Path("configuration/pretty")
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
            bean.setName(config.getName());
            bean.setInterval(config.getInterval());
            bean.setType(config.getType());
            bean.setHosts(config.getHosts());

            List<MoskitoAnalyzeProducerBean> producers = new ArrayList<>();
            for (ProducerConfig producerConfig : config.getProducers()) {
                MoskitoAnalyzeProducerBean producer = new MoskitoAnalyzeProducerBean();
                producer.setCaption(producerConfig.getCaption());
                producer.setValue(producerConfig.getValue());
                producer.setStat(producerConfig.getStat());
                producer.setProducer(producerConfig.getProducer());

                producers.add(producer);
            }

            bean.setProducers(producers);

            chartBeans.add(bean);
        }

        // Filling request
        MoskitoAnalyzeChartsResponse response = new MoskitoAnalyzeChartsResponse();
        response.setCharts(chartBeans);

        return response;
    }
}
