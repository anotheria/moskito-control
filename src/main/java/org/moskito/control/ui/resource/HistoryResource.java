package org.moskito.control.ui.resource;

import org.moskito.control.core.history.StatusUpdateHistoryItem;
import org.moskito.control.core.history.StatusUpdateHistoryRepository;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * This resource serves history requests for one app.
 *
 * @author lrosenberg
 * @since 13.06.13 17:02
 */
@Path("/history")
public class HistoryResource {

	@GET
	@Path("/{appName}")
	@Produces(MediaType.APPLICATION_JSON)
	public HistoryBean control(@PathParam("appName") String appName) {

		List<HistoryItemBean> beans = new ArrayList<HistoryItemBean>();
		List<StatusUpdateHistoryItem> items = StatusUpdateHistoryRepository.getInstance().getHistoryForApplication(appName);

		for (StatusUpdateHistoryItem item : items) {
			beans.add(HistoryItemBean.fromStatusUpdateHistoryItem(item));
		}

		return new HistoryBean(appName, beans);
	}

	@GET
	@Path("/{appName}/{component}")
	@Produces(MediaType.APPLICATION_JSON)
	public HistoryBean getComponentHistory(@PathParam("appName") String appName, @PathParam("component") String component) {
		List<HistoryItemBean> beans = new ArrayList<>();
		List<StatusUpdateHistoryItem> items = StatusUpdateHistoryRepository.getInstance().getHistoryForApplication(appName);

		for (StatusUpdateHistoryItem item : items) {
			if (component.equals(item.getComponent().getName())) {
				beans.add(HistoryItemBean.fromStatusUpdateHistoryItem(item));
			}
		}

		return new HistoryBean(appName, beans);
	}
}
