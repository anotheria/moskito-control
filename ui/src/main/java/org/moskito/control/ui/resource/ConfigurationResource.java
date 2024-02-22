package org.moskito.control.ui.resource;

import io.swagger.v3.oas.annotations.servers.Server;
import org.moskito.control.config.MoskitoControlConfiguration;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Returns JSON representation of Moskito-Control configuration
 * used for Settings page.
 *
 * @author sstreltsov
 */
@Path("/configuration")
@Produces(MediaType.APPLICATION_JSON)
@Server(url = "/rest")
public class ConfigurationResource {

	@GET
	public MoskitoControlConfiguration getStatus(){
		return MoskitoControlConfiguration.getConfiguration();
	}
}
