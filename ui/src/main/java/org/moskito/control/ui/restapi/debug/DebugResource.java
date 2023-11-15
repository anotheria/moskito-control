package org.moskito.control.ui.restapi.debug;

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
import java.util.List;

/**
 * Used for providing additional debug information for development and testing purposes.
 *
 * @author lrosenberg
 * @since 27.10.20 10:27
 */
@Path("debug")
@Produces(MediaType.APPLICATION_JSON)
public class DebugResource {

	@GET
	public ReplyObject debug(){
		System.out.println("DEBUG RESOURCE CALLED ");
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
