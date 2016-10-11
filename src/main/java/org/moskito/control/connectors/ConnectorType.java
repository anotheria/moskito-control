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
	 * DistributeMe (same as RMI) connector. (for backward compatibility)
	 */
	DISTRIBUTEME,
	/**
	 * RMI connector.
	 */
	RMI,
	/**
	 * No operation connector is used for testing purposes.
	 */
	NOOP;
}
