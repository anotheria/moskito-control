package org.moskito.control.ui.resource;

import net.anotheria.util.TimeUnit;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.core.ApplicationRepository;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Simple resource used to mute / unmute notifications.
 * @author strel
 */
@Path("/notifications")
public class NotificationsConfiguratorResource {

	@GET
	@Path("/mute")
	public void mute() {
		final long delay = TimeUnit.MINUTE.getMillis(MoskitoControlConfiguration.getConfiguration().getNotificationsMutingTime());
        ApplicationRepository.getInstance().getEventsDispatcher().mute(delay);
	}

	@GET
	@Path("/unmute")
	public void unmute() {
		ApplicationRepository.getInstance().getEventsDispatcher().unmute();
	}
}
