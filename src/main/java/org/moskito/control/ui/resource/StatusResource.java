package org.moskito.control.ui.resource;

import org.moskito.control.core.Application;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.updater.ApplicationStatusUpdater;
import org.moskito.control.core.updater.ChartDataUpdater;

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

			appStatusBean.setChartUpdaterRunCount(app.getChartUpdaterRunCount());
			appStatusBean.setChartUpdaterSuccessCount(app.getChartUpdaterSuccessCount());
			appStatusBean.setStatusUpdaterRunCount(app.getStatusUpdaterRunCount());
			appStatusBean.setStatusUpdaterSuccessCount(app.getStatusUpdaterSuccessCount());

			appStatusBean.setColor(app.getWorstHealthStatus());


			ret.addStatus(app.getName(), appStatusBean);
		}

		ret.addUpdaterStatus("status", ApplicationStatusUpdater.getInstance().getStatus());
		ret.addUpdaterStatus("charts", ChartDataUpdater.getInstance().getStatus());

		return ret;
	}
}
