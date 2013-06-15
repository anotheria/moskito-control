package org.moskito.control.ui.resource;

import net.anotheria.util.NumberUtils;
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
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 13.06.13 17:02
 */
@Path("/history/{appName}")
public class HistoryResource {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public HistoryBean control(@PathParam("appName") String appName){

		List<HistoryItemBean> beans = new ArrayList<HistoryItemBean>();
		List<StatusUpdateHistoryItem> items =  StatusUpdateHistoryRepository.getInstance().getHistoryForApplication(appName);
		if (items==null)
			return new HistoryBean(appName, beans);
		for (StatusUpdateHistoryItem item : items){
			HistoryItemBean b = new HistoryItemBean();
			b.setComponentName(item.getComponent().getName());
			b.setIsoTimestamp(NumberUtils.makeISO8601TimestampString(item.getTimestamp()));
			b.setTimestamp(item.getTimestamp());
			b.setOldStatus(item.getOldStatus().getHealth().name());
			b.setNewStatus(item.getNewStatus().getHealth().name());
			b.setOldMessages(item.getOldStatus().getMessages());
			b.setNewMessages(item.getNewStatus().getMessages());
			beans.add(b);
		}

		return new HistoryBean(appName, beans);
	}

}
