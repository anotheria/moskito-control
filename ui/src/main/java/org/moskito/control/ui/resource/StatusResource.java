package org.moskito.control.ui.resource;

import org.moskito.control.core.ComponentRepository;
import org.moskito.control.core.View;
import org.moskito.control.core.updater.ChartDataUpdater;
import org.moskito.control.core.updater.ComponentStatusUpdater;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
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


		List<View> views =  ComponentRepository.getInstance().getViews();
		for (View view : views){
			ViewStatusBean bean = new ViewStatusBean();
			bean.setColor(view.getWorstHealthStatus());
			ret.addStatus(view.getName(), bean);
		}

		ret.addUpdaterStatus("status", ComponentStatusUpdater.getInstance().getStatus());
		ret.addUpdaterStatus("charts", ChartDataUpdater.getInstance().getStatus());

		return ret;
		/*
		List<Application> apps = ComponentRepository.getInstance ().getApplications();
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


		return ret;
		*/
	}
}
