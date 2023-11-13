package org.moskito.control.ui.resource;

import net.anotheria.util.TimeUnit;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.core.ComponentRepository;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

/**
 * Resource used to mute / unmute notifications.
 * @author strel
 */
@Path("/notifications")
public class NotificationsConfiguratorResource {

	@GET
	@Path("/mute")
	public NotificationsConfiguratorReply mute() {
		NotificationsConfiguratorReply reply = new NotificationsConfiguratorReply();

		final long delay = TimeUnit.MINUTE.getMillis(MoskitoControlConfiguration.getConfiguration().getNotificationsMutingTime());
        ComponentRepository.getInstance().getEventsDispatcher().mute(delay);
        reply.setResult(true);

        return reply;
	}

	@GET
	@Path("/unmute")
	public NotificationsConfiguratorReply unmute() {
		NotificationsConfiguratorReply reply = new NotificationsConfiguratorReply();
		ComponentRepository.getInstance().getEventsDispatcher().unmute();
		reply.setResult(true);

		return reply;
	}
}
