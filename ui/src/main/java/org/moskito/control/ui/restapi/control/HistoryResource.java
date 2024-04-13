package org.moskito.control.ui.restapi.control;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.moskito.control.core.Repository;
import org.moskito.control.core.View;
import org.moskito.control.core.history.StatusUpdateHistoryItem;
import org.moskito.control.core.history.StatusUpdateHistoryRepository;
import org.moskito.control.ui.restapi.ReplyObject;

import java.util.ArrayList;
import java.util.List;

@Path("history")
@Produces(MediaType.APPLICATION_JSON)
@Server(url = "/api/v2")
@Tag(name = "History API", description = "API for retrieving global history information about the component status changes")
public class HistoryResource {
    @Operation(summary = "Returns all status changes",
            description = "Returns all status changes."
    )
    @GET
    public ReplyObject getHistory(){
        List<HistoryItemBean> beans = new ArrayList<>();
        List<StatusUpdateHistoryItem> items = StatusUpdateHistoryRepository.getInstance().getHistoryForApplication();

        for (StatusUpdateHistoryItem item : items) {
            beans.add(HistoryItemBean.fromStatusUpdateHistoryItem(item));
        }

        return ReplyObject.success("history", beans);
    }


    @Operation(summary = "Returns all status changes for the given view",
            description = "Returns all status changes for components in the provided view")
    @GET
    @Path("/{view}")
    @Produces(MediaType.APPLICATION_JSON)
    public ReplyObject getComponentHistory(@PathParam("view") String viewName) {
        Repository repository = Repository.getInstance();
        View view = repository.getView(viewName);


        List<HistoryItemBean> beans = new ArrayList<>();
        List<StatusUpdateHistoryItem> items = view.getViewHistory();

        for (StatusUpdateHistoryItem item : items) {
            beans.add(HistoryItemBean.fromStatusUpdateHistoryItem(item));
        }

        return ReplyObject.success("history", beans);
    }

}
