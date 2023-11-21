package org.moskito.control.ui.restapi.config;

import io.swagger.v3.oas.annotations.servers.Server;
import org.moskito.control.config.ComponentConfig;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.core.Component;
import org.moskito.control.core.ComponentRepository;
import org.moskito.control.ui.restapi.ReplyObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

@Path("configuration")
@Produces(MediaType.APPLICATION_JSON)
@Server(url = "/rest")
public class ConfigResource {


    @Path("components") @GET
    public ReplyObject getComponents(){
        ComponentConfig components[] = MoskitoControlConfiguration.getConfiguration().getComponents();
        List<ComponentConfig> ret = Arrays.asList(components);
        return ReplyObject.success("components", ret);
    }

    @Path("components/{name}") @DELETE
    public ReplyObject deleteComponent(@PathParam("name") String name){
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
    public ReplyObject deleteChart(@PathParam("name") String name){
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
    public ReplyObject deleteView(@PathParam("name") String name){
        MoskitoControlConfiguration.getConfiguration().removeView(name);
        //for new we have to manually remove it from the repository.
        ComponentRepository.getInstance().removeView(name);
        return ReplyObject.success( );
    }


}
