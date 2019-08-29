package org.moskito.control.ui.resource.component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.moskito.control.core.Component;
import org.moskito.control.core.ComponentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                component.setStatus(request.getStatus());
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

        ret.setCategory("Dynamic");
        ret.setStatus(toMap.getStatus());
        ret.setTags(toMap.getTags());
        ret.setDynamic(true);

        return ret;
    }
}
