package org.moskito.control.connectors.response;

import org.moskito.controlagent.data.nowrunning.EntryPoint;

import java.util.LinkedList;
import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 27.10.20 10:43
 */
public class ConnectorNowRunningResponse extends ConnectorResponse{
	private List<EntryPoint> entryPoints;

	public ConnectorNowRunningResponse(){
		entryPoints = new LinkedList<>();
	}

	public ConnectorNowRunningResponse(List<EntryPoint> entryPoints) {
		this.entryPoints = entryPoints;
	}

	public List<EntryPoint> getEntryPoints() {
		return entryPoints;
	}

	public void setEntryPoints(List<EntryPoint> entryPoints) {
		this.entryPoints = entryPoints;
	}

	@Override
	public String toString() {
		return "ConnectorNowRunningResponse{" +
				"entryPoints=" + entryPoints +
				'}';
	}
}
