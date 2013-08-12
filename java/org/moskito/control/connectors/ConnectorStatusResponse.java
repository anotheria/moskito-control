package org.moskito.control.connectors;

import org.moskito.control.core.Status;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 02.06.13 18:53
 */
public class ConnectorStatusResponse extends ConnectorResponse{
	/**
	 * Target application's status.
	 */
	private Status status;

	public ConnectorStatusResponse(Status status){
		this.status = status;
	}

	public Status getStatus(){
		return status;
	}

	public String toString(){
		return status.toString();
	}
}
