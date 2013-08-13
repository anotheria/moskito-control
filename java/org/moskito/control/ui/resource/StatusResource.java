package org.moskito.control.ui.resource;

import org.moskito.control.core.Application;
import org.moskito.control.core.ApplicationRepository;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.08.13 18:20
 */
@Path("/status")
@Produces(MediaType.APPLICATION_JSON)
public class StatusResource {

	@GET
	public StatusReplyObject getStatus(){
		StatusReplyObject ret = new StatusReplyObject();

		List<Application> apps = ApplicationRepository.getInstance ().getApplications();
		for (Application app : apps){
			ApplicationStatusBean appStatusBean = new ApplicationStatusBean();
			appStatusBean.setLastStatusUpdaterRun(app.getLastStatusUpdaterRun());
			appStatusBean.setLastStatusUpdaterSuccess(app.getLastStatusUpdaterSuccess());
			appStatusBean.setLastChartUpdaterRun(app.getLastChartUpdaterRun());
			appStatusBean.setLastChartUpdaterSuccess(app.getLastChartUpdaterSuccess());


			ret.addStatus(app.getName(), appStatusBean);
		}

		return ret;
	}
}
