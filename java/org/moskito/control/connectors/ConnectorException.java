package org.moskito.control.connectors;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 27.08.13 08:51
 */
public class ConnectorException extends RuntimeException{
	public ConnectorException() {
	}

	public ConnectorException(String message) {
		super(message);
	}

	public ConnectorException(String message, Throwable cause) {
		super(message, cause);
	}
}
