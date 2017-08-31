package org.moskito.control.ui.resource.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.moskito.control.config.MoskitoControlConfiguration;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Returns JSON representation of Moskito-Control configuration
 * used for Settings page.
 *
 * @author sstreltsov
 */
@Path("/configuration")
@Produces(MediaType.APPLICATION_JSON)
public class ConfigurationResource {

	@GET
	public MoskitoControlConfiguration getConfiguration(){
		return MoskitoControlConfiguration.getConfiguration();
	}

	@GET
	@Path("/pretty")
	public String getPrettyConfiguration() {
		MoskitoControlConfiguration config = MoskitoControlConfiguration.getConfiguration();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonOutput = gson.toJson(config);

		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(jsonOutput);

		return gson.toJson(je);
	}

	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateConfiguration(String configString) {
		Gson gson = new GsonBuilder().create();
		MoskitoControlConfiguration updatedConfig = gson.fromJson(configString, MoskitoControlConfiguration.class);

		MoskitoControlConfiguration config = MoskitoControlConfiguration.getConfiguration();

	}
}
