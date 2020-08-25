package org.moskito.controlagent.endpoints.rmi;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 09.04.14 14:02
 */
public class AgentServiceException extends Exception{
 	public AgentServiceException(String message){
		super(message);
	}

	public AgentServiceException(String message, Throwable cause){
		super(message, cause);
	}
}
