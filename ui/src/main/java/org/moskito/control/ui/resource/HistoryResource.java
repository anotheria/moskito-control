package org.moskito.control.ui.resource;

import io.swagger.v3.oas.annotations.servers.Server;
import org.moskito.control.core.ComponentRepository;
import org.moskito.control.core.View;
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
@Server(url = "/rest")
public class HistoryResource {

	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public HistoryBean history( String appName) {

		List<HistoryItemBean> beans = new ArrayList<HistoryItemBean>();
		List<StatusUpdateHistoryItem> items = StatusUpdateHistoryRepository.getInstance().getHistoryForApplication();

		for (StatusUpdateHistoryItem item : items) {
			beans.add(HistoryItemBean.fromStatusUpdateHistoryItem(item));
		}

		return new HistoryBean(beans);
	}

	@GET
	@Path("/{view}")
	@Produces(MediaType.APPLICATION_JSON)
	public HistoryBean getComponentHistory(@PathParam("view") String viewName) {
		ComponentRepository repository = ComponentRepository.getInstance();
		View view = repository.getView(viewName);


		List<HistoryItemBean> beans = new ArrayList<>();
		List<StatusUpdateHistoryItem> items = view.getViewHistory();

		for (StatusUpdateHistoryItem item : items) {
			beans.add(HistoryItemBean.fromStatusUpdateHistoryItem(item));
		}

		return new HistoryBean(beans);
	}
}
