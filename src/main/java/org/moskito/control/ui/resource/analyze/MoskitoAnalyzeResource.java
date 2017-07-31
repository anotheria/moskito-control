package org.moskito.control.ui.resource.analyze;

import org.moskito.control.config.MoskitoAnalyzeChartConfig;
import org.moskito.control.config.MoskitoAnalyzeConfig;

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
        response.setHosts( analyzeConfig.getHosts() );

        return response;
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
            bean.setProducer(config.getProducer());
            bean.setStat(config.getStat());
            bean.setValue(config.getValue());
            bean.setType(config.getType());

            chartBeans.add(bean);
        }

        // Filling request
        MoskitoAnalyzeChartsResponse response = new MoskitoAnalyzeChartsResponse();
        response.setCharts(chartBeans);

        return response;
    }
}
