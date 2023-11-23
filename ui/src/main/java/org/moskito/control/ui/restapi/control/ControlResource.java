package org.moskito.control.ui.restapi.control;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.moskito.control.core.Component;
import org.moskito.control.core.ComponentRepository;
import org.moskito.control.core.View;
import org.moskito.control.ui.restapi.ReplyObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("control")
@Produces(MediaType.APPLICATION_JSON)
@Server(url = "/api/v2")
@OpenAPIDefinition(info =
@Info(
        title = "MoSKito Control REST API",
        version = "2.0",
        description = "This is the API for handling of moskito configuration, retrieval of statuses and components, as well as additional info. It should replace all existing APIs.",
        license = @License(name = "MIT", url = "https://opensource.org/license/mit/"),
        contact = @Contact(url = "http://moskito.org", name = "MoSKito Team", email = "info@moskito.org")
)
)
public class ControlResource {
    @GET public ReplyObject getControl(){

        ComponentRepository repository = ComponentRepository.getInstance();
        List<View> views = repository.getViews();
        List<ViewContainerBean> viewBeans = new ArrayList<>();

        for (View view : views){
            ViewContainerBean viewContainerBean = new ViewContainerBean();
            viewContainerBean.setName(view.getName());
            viewContainerBean.setViewColor(view.getWorstHealthStatus());

            List<Component> components = view.getComponents();
            for (Component c : components){
                ComponentBean cBean = new ComponentBean();
                cBean.setName(c.getName());
                cBean.setColor(c.getHealthColor());
                cBean.setCategory(c.getCategory());
                cBean.setMessages(c.getStatus().getMessages());
                cBean.setLastUpdateTimestamp(c.getLastUpdateTimestamp());
                viewContainerBean.addComponent(cBean);
            }

            viewBeans.add(viewContainerBean);
        }


        return ReplyObject.success("views", viewBeans);
    }
}
