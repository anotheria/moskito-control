package org.moskito.control.connectors;

import org.moskito.control.core.Status;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 02.06.13 18:53
 */
public class ConnectorResponse {
	private Status status;

	public ConnectorResponse(Status status){
		this.status = status;
	}

	public Status getStatus(){
		return status;
	}

	public String toString(){
		return status.toString();
	}
}
