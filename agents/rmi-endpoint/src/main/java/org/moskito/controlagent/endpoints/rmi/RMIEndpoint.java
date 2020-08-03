package org.moskito.controlagent.endpoints.rmi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * This is a utility class that allows to start the embedded endpoint with one method call.
 *
 * @author lrosenberg
 * @since 09.04.14 16:54
 */
public final class RMIEndpoint {

	private static Logger log = LoggerFactory.getLogger(RMIEndpoint.class);

	public static void startRMIEndpoint() throws RMIEndpointException{
		String clazzName = "org.moskito.controlagent.endpoints.rmi.generated.AgentServer";
		String methodName = "createServiceAndRegisterLocally";

		Class agentServerClass = null;
		try {
			agentServerClass = Class.forName(clazzName);
		} catch (ClassNotFoundException e) {
			throw new RMIEndpointException("Couldn't find DistributeMe Server class "+clazzName, e);
		}

		try {
			//call the static methods
			agentServerClass.getMethod("init").invoke(null);
			agentServerClass.getMethod(methodName).invoke(null);
		} catch (NoSuchMethodException e) {
			throw new RMIEndpointException("Couldn't find my target method in agent class - report to moskito-users@lists.anotheria.net", e);
		} catch (IllegalAccessException e) {
			throw new RMIEndpointException("Couldn't access my target method in agent class - report to moskito-users@lists.anotheria.net", e);
		} catch (InvocationTargetException e) {
			throw new RMIEndpointException("Target method access produced an exception - report to moskito-users@lists.anotheria.net", e);
		}


	}

	//prevent instantiation.
	private RMIEndpoint(){}
}
