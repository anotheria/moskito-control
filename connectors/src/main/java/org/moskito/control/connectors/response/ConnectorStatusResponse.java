package org.moskito.control.connectors.response;

import org.moskito.control.common.Status;

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

	/**
	 * Now Running Count.
	 * @since version2 of the protocol.
	 */
	private int nowRunningCount;


	public ConnectorStatusResponse(Status status){
		this.status = status;
	}

	public Status getStatus(){
		return status;
	}

	public String toString(){
		return status.toString();
	}

	public int getNowRunningCount() {
		return nowRunningCount;
	}

	public void setNowRunningCount(int nowRunningCount) {
		this.nowRunningCount = nowRunningCount;
	}
}
