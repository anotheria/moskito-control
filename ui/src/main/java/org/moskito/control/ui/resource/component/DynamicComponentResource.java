package org.moskito.control.ui.resource.component;

import org.moskito.control.core.Component;
import org.moskito.control.core.ComponentRepository;
import org.moskito.control.common.HealthColor;
import org.moskito.control.common.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Collections;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/push")
public class DynamicComponentResource {

    private final static Logger LOG = LoggerFactory.getLogger(DynamicComponentResource.class);

    @POST
    @Path("/status")
    public Response pushStatus(PushStatusRequest request) {
        try {
            Component component = ComponentRepository.getInstance().getComponent(request.getName());
            if (component != null) {
                component.setStatus(mapStatus(request));
                component.setAttributes(request.getAttributes());
                return Response.ok().build();
            }

            ComponentRepository.getInstance().addComponent(map(request));
            return Response.ok().build();
        } catch (Exception e) {
            LOG.error("pushStatus() failed: for <{}> with errors: <{}>", request, e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Component map(PushStatusRequest toMap) {
        Component ret = new Component(toMap.getName());

        ret.setCategory(
        		toMap.getCategory()==null ? "dynamic" : toMap.getCategory()
				);

        ret.setStatus(mapStatus(toMap));
        ret.setTags(toMap.getTags());
        ret.setDynamic(true);
        ret.setAttributes(toMap.getAttributes());

        return ret;
    }

    private Status mapStatus(PushStatusRequest request){
		if (request.getStatus() == null){
			return new Status(HealthColor.NONE, "No status submitted");
		}
		Status ret = new Status();
		ret.setHealth(request.getStatus());
		ret.setMessages(request.getMessages() == null ?
				Collections.emptyList(): request.getMessages());
		return ret;
	}
}
