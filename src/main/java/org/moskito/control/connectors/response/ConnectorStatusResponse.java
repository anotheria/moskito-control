package org.moskito.control.connectors.response;

import org.moskito.control.core.status.Status;

/**
 * Contains status data.
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
