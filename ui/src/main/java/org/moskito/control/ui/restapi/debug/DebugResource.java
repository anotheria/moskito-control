package org.moskito.control.ui.restapi.debug;

import io.swagger.v3.oas.annotations.servers.Server;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.connectors.Connector;
import org.moskito.control.connectors.ConnectorFactory;
import org.moskito.control.connectors.response.ConnectorNowRunningResponse;
import org.moskito.control.core.Component;
import org.moskito.control.core.ComponentRepository;
import org.moskito.control.ui.restapi.ReplyObject;
import org.moskito.controlagent.data.nowrunning.EntryPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Used for providing additional debug information for development and testing purposes.
 *
 * @author lrosenberg
 * @since 27.10.20 10:27
 */
@Path("debug")
@Produces(MediaType.APPLICATION_JSON)
@Server(url = "/api/v2")
public class DebugResource {

	@GET
	public ReplyObject debug(){
		System.out.println("DEBUG RESOURCE CALLED ");
		return ReplyObject.success();
	}

	@Path("saveconfig")
	@GET public ReplyObject saveConfig(){

		MoskitoControlConfiguration configuration = MoskitoControlConfiguration.getConfiguration();
		Gson gson = new GsonBuilder().
				setExclusionStrategies(new ExclusionStrategy() {
					@Override
					public boolean shouldSkipField(FieldAttributes f) {
						return f.getAnnotation(SerializedName.class) == null;
					}

					@Override
					public boolean shouldSkipClass(Class<?> clazz) {
						return false;
					}
				}).
				setPrettyPrinting().disableHtmlEscaping().create();

		String configAsString = gson.toJson(configuration);
		try {
			FileOutputStream fOut = new FileOutputStream("moskito-control-configuration/moskito-control-new.json");
			fOut.write(configAsString.getBytes());
			fOut.close();
		}catch(IOException e){
			return ReplyObject.error(e);
		}

		return ReplyObject.success();
	}
	@GET
	@Path("nowrunning")
	public ReplyObject getNowRunning(){

		ReplyObject ret = ReplyObject.success();

		List<Component> componentList = ComponentRepository.getInstance().getComponents();
		for (Component component : componentList){
			Connector connector = ConnectorFactory.createConnector(component.getConfiguration().getConnectorType());
			connector.configure(component.getConfiguration());
			if (!connector.supportsNowRunning()){
				continue;
			}
			ConnectorNowRunningResponse response = connector.getNowRunning();
			List<EntryPoint> entryPoints = response.getEntryPoints();
			if (entryPoints==null)
				continue;
			for (EntryPoint p : entryPoints){
				ret.addResult(component.getName()+"."+p.getProducerId(), p.getCurrentMeasurements());
			}

		}

		return ret;
	}

	@GET
	@Path("nowrunningpast")
	public ReplyObject getNowRunningPastMeasurements(){

		ReplyObject ret = ReplyObject.success();

		List<Component> componentList = ComponentRepository.getInstance().getComponents();
		for (Component component : componentList){
			Connector connector = ConnectorFactory.createConnector(component.getConfiguration().getConnectorType());
			connector.configure(component.getConfiguration());
			if (!connector.supportsNowRunning()){
				continue;
			}
			ConnectorNowRunningResponse response = connector.getNowRunning();
			List<EntryPoint> entryPoints = response.getEntryPoints();
			if (entryPoints==null)
				continue;
			for (EntryPoint p : entryPoints){
				ret.addResult(component.getName()+"."+p.getProducerId(), p.getPastMeasurements());
			}

		}

		return ret;
	}

}
