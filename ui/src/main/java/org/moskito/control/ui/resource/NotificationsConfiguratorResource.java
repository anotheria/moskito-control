package org.moskito.control.ui.resource;

import io.swagger.v3.oas.annotations.servers.Server;
import net.anotheria.util.TimeUnit;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.core.Repository;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

/**
 * Resource used to mute / unmute notifications.
 * @author strel
 */
@Path("/notifications")
@Server(url = "/rest")
public class NotificationsConfiguratorResource {

	@GET
	@Path("/mute")
	public NotificationsConfiguratorReply mute() {
		NotificationsConfiguratorReply reply = new NotificationsConfiguratorReply();

		final long delay = TimeUnit.MINUTE.getMillis(MoskitoControlConfiguration.getConfiguration().getNotificationsMutingTime());
        Repository.getInstance().getEventsDispatcher().mute(delay);
        reply.setResult(true);

        return reply;
	}

	@GET
	@Path("/unmute")
	public NotificationsConfiguratorReply unmute() {
		NotificationsConfiguratorReply reply = new NotificationsConfiguratorReply();
		Repository.getInstance().getEventsDispatcher().unmute();
		reply.setResult(true);

		return reply;
	}
}
