package org.moskito.control.ui.resource;

import io.swagger.v3.oas.annotations.servers.Server;
import org.moskito.control.config.MoskitoControlConfiguration;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
