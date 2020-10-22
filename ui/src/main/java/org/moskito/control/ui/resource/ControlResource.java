package org.moskito.control.ui.resource;

import org.moskito.control.core.Component;
import org.moskito.control.core.ComponentRepository;
import org.moskito.control.core.View;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Resource for control REST API.
 *
 * @author lrosenberg
 * @since 05.06.13 22:20
 */
@Path("/control")
@Produces(MediaType.APPLICATION_JSON)
public class ControlResource {

	@GET @Produces(MediaType.APPLICATION_JSON)
	public ControlBean control(){
		ControlBean ret = new ControlBean();
		ComponentRepository repository = ComponentRepository.getInstance();
		List<View> views = repository.getViews();

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

			ret.addView(viewContainerBean);
		}


		return ret;
	}
}
