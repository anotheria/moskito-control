package org.moskito.control.ui.resource;

import org.moskito.control.core.Application;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.Component;

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
		ApplicationRepository repository = ApplicationRepository.getInstance();
		List<Application> applications = repository.getApplications();

		for (Application application : applications){
			ApplicationContainerBean acb = new ApplicationContainerBean();
			acb.setName(application.getName());
			acb.setApplicationColor(application.getWorstHealthStatus());

			List<Component> components = application.getComponents();
			for (Component c : components){
				ComponentBean cBean = new ComponentBean();
				cBean.setName(c.getName());
				cBean.setColor(c.getHealthColor());
				cBean.setCategory(c.getCategory());
				cBean.setMessages(c.getStatus().getMessages());
				cBean.setLastUpdateTimestamp(c.getLastUpdateTimestamp());
				acb.addComponent(cBean);
			}

			ret.addApplication(acb);
		}


		return ret;
	}
}
