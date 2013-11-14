package org.moskito.control.connectors;

/**
 * Base exception class for exceptions that can be thrown by a Connector.
 *
 * @author lrosenberg
 * @since 27.08.13 08:51
 */
public class ConnectorException extends RuntimeException{
	/**
	 * Creates new exception.
	 */
	public ConnectorException() {
	}

	/**
	 * Creates new exception.
	 */
	public ConnectorException(String message) {
		super(message);
	}

	/**
	 * Creates new exception.
	 */
	public ConnectorException(String message, Throwable cause) {
		super(message, cause);
	}
}
