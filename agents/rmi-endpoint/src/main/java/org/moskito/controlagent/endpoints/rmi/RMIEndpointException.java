package org.moskito.controlagent.endpoints.rmi;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 09.04.14 16:59
 */
public class RMIEndpointException extends Exception {
 	public RMIEndpointException(String name){
		super(name);
	}
	public RMIEndpointException(String name, Throwable t){
		super(name, t);
	}
}
