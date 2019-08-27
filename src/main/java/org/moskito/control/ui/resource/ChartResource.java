package org.moskito.control.ui.resource;

import org.moskito.control.core.Application;
import org.moskito.control.core.View;
import org.moskito.control.ui.action.MainViewAction;
import org.moskito.control.ui.bean.ChartPointBean;

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
@Path("/charts")
@Produces(MediaType.APPLICATION_JSON)
public class ChartResource {

	@GET
	@Path("/points/{viewName}")
	public ChartContainerBean chartPoints(@PathParam("viewName") String appName){
		//TODO revisit
		Application app = null;//ComponentRepository.getInstance().getApplication(appName);
		if (app==null)
			throw new IllegalArgumentException("Couldn't find application for "+appName);
		ChartContainerBean ret = new ChartContainerBean();

		List<org.moskito.control.ui.bean.ChartBean> viewBeans = MainViewAction.prepareChartData((View)null);
		List<ChartResponseBean> restBeans = new ArrayList<ChartResponseBean>();
		for (org.moskito.control.ui.bean.ChartBean viewCB : viewBeans){
			ChartBean restCB = new ChartBean();
			restCB.setLineNames(viewCB.getLineNames());
			restCB.setPoints(viewCB.getPoints());
			restCB.setName(viewCB.getName());
			restCB.setLegend(viewCB.getLegend());
			restBeans.add(restCB);
		}

		ret.setCharts(restBeans);
		return ret;
	}

	@GET
	@Path("/lines/{appName}")
	public ChartContainerBean chartLines(@PathParam("appName") String appName){
		//TODO REVISIT
		Application app = null;//ComponentRepository.getInstance().getApplication(appName);
		if (app==null)
			throw new IllegalArgumentException("Couldn't find application for "+appName);
		ChartContainerBean ret = new ChartContainerBean();

		List<org.moskito.control.ui.bean.ChartBean> viewBeans = MainViewAction.prepareChartData((View)null);
		List<ChartResponseBean> restBeans = new ArrayList<ChartResponseBean>();
		for (org.moskito.control.ui.bean.ChartBean viewCB : viewBeans){
			ChartLinesBean restCB = new ChartLinesBean();

			restCB.setName(viewCB.getName());
			for (int i=0; i<viewCB.getLineNames().size(); i++){
				ChartLineBean chartLineBean = new ChartLineBean();
				chartLineBean.setLineName(viewCB.getLineNames().get(i));
				restCB.addChartLineBean(chartLineBean);
			}

			int l = viewCB.getLineNames().size();
			for (int i=0;i<viewCB.getPoints().size(); i++){
				ChartPointBean cpb = viewCB.getPoints().get(i);
				restCB.addCaption(cpb.getCaption());
				restCB.addTimestamp(cpb.getTimestamp());
				for (int t=0; t<l; t++){
					restCB.getLines().get(t).addValue(cpb.getValueAt(t));
				}
			}

			restBeans.add(restCB);
		}

		ret.setCharts(restBeans);
		return ret;
	}
}
