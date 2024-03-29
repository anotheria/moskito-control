package org.moskito.control.ui.resource;

import io.swagger.v3.oas.annotations.servers.Server;
import org.moskito.control.core.Repository;
import org.moskito.control.core.View;
import org.moskito.control.core.history.StatusUpdateHistoryItem;
import org.moskito.control.core.history.StatusUpdateHistoryRepository;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
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
	public HistoryBean history() {

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
		Repository repository = Repository.getInstance();
		View view = repository.getView(viewName);


		List<HistoryItemBean> beans = new ArrayList<>();
		List<StatusUpdateHistoryItem> items = view.getViewHistory();

		for (StatusUpdateHistoryItem item : items) {
			beans.add(HistoryItemBean.fromStatusUpdateHistoryItem(item));
		}

		return new HistoryBean(beans);
	}
}
