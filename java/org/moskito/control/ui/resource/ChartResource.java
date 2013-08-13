package org.moskito.control.ui.resource;

import org.moskito.control.core.Application;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.ui.action.MainViewAction;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 09.08.13 15:54
 */
@Path("/charts/{appName}")
@Produces(MediaType.APPLICATION_JSON)
public class ChartResource {

	@GET
	public ChartContainerBean charts(@PathParam("appName") String appName){
		Application app = ApplicationRepository.getInstance().getApplication(appName);
		System.out.println("App "+app+" for "+appName);
		if (app==null)
			throw new IllegalArgumentException("Couldn't find application for "+appName);
		ChartContainerBean ret = new ChartContainerBean();

		List<org.moskito.control.ui.bean.ChartBean> viewBeans = MainViewAction.prepareChartData(app);
		List<ChartBean> restBeans = new ArrayList<ChartBean>();
		for (org.moskito.control.ui.bean.ChartBean viewCB : viewBeans){
			ChartBean restCB = new ChartBean();
			restCB.setLineNames(viewCB.getLineNames());
			restCB.setPoints(viewCB.getPoints());
			restCB.setName(viewCB.getName());
			restBeans.add(restCB);
		}

		ret.setCharts(restBeans);
		return ret;
	}

}
