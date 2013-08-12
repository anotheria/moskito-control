package org.moskito.control.connectors;

/**
 * Configured connector types for reference in config.
 *
 * @author lrosenberg
 * @since 24.04.13 11:22
 */
public enum ConnectorType {
	/**
	 * Http connection connector.
	 */
	HTTP,
	/**
	 * DistributeMe (RMI) connector.
	 */
	DISTRIBUTEME,
	/**
	 * No operation connector is used for testing purposes.
	 */
	NOOP;
}
