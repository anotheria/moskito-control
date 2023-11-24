package org.moskito.control.ui.restapi.config;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.servers.Server;
import org.moskito.control.config.ChartConfig;
import org.moskito.control.config.ComponentConfig;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.core.Component;
import org.moskito.control.core.ComponentRepository;
import org.moskito.control.ui.restapi.ReplyObject;
import org.moskito.control.ui.restapi.config.bean.ComponentPO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

@Path("configuration")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Server(url = "/rest")
@Server(url = "/api/v2")
public class ConfigResource {


    @Path("components") @GET
    public ReplyObject getComponents(){
        ComponentConfig components[] = MoskitoControlConfiguration.getConfiguration().getComponents();
        List<ComponentConfig> ret = Arrays.asList(components);
        return ReplyObject.success("components", ret);
    }

    @Path("components")
    @POST
    public ReplyObject addComponent(ComponentPO componentPO){
        ComponentConfig toAdd = componentPO.toComponentConfig();
        MoskitoControlConfiguration.getConfiguration().addComponent(toAdd);
        ComponentRepository.getInstance().addComponent(new Component(toAdd));

        return ReplyObject.success();
    }

    @Path("components/{name}") @DELETE
    public ReplyObject deleteComponent(
            @Parameter(description = "Name of the component to remove from the config", required = true)
            @PathParam("name") String name){
        MoskitoControlConfiguration.getConfiguration().removeComponent(name);
        //for new we have to manually remove it from the repository.
        ComponentRepository.getInstance().removeComponent(name);
        return ReplyObject.success( );
    }


    @Path("charts") @GET
    public ReplyObject getCharts(){
        ChartConfig charts[] = MoskitoControlConfiguration.getConfiguration().getCharts();
        List<ChartConfig> ret = Arrays.asList(charts);
        return ReplyObject.success("charts", ret);
    }

    @Path("charts/{name}") @DELETE
    public ReplyObject deleteChart(
            @Parameter(description = "Name of the chart to remove from the config", required = true)
            @PathParam("name") String name){
        MoskitoControlConfiguration.getConfiguration().removeChart(name);
        //for new we have to manually remove it from the repository.
        ComponentRepository.getInstance().removeChart(name);
        return ReplyObject.success( );
    }

    @Path("views") @GET
    public ReplyObject getViews(){
        return ReplyObject.success("views", MoskitoControlConfiguration.getConfiguration().getViews());
    }

    @Path("views/{name}") @DELETE
    public ReplyObject deleteView(
            @Parameter(description = "Name of the view to remove from the config", required = true)
            @PathParam("name") String name){
        MoskitoControlConfiguration.getConfiguration().removeView(name);
        //for new we have to manually remove it from the repository.
        ComponentRepository.getInstance().removeView(name);
        return ReplyObject.success( );
    }


}
