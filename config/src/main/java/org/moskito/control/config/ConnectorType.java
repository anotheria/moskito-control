package org.moskito.control.config;

/**
 * Configured connector types for reference in config.
 *
 * @author lrosenberg
 * @since 24.04.13 11:22
 */
public enum ConnectorType {
	/**
	 * Http connection connector. Connects to moskito-control-agent endpoint. @see moskito-control-agent-http-endpoint (maven artifact).
	 */
	HTTP,
	/**
	 * Generic Http URL connector.
	 */
	URL,
	/**
	 * DistributeMe (same as RMI) connector. (for backward compatibility).
	 * moskito-control-agent-rmi-endpoint (maven artifact)..
	 */
	DISTRIBUTEME,
	/**
	 * RMI connector.
	 */
	RMI,
	/**
	 * JDBC connector.
	 */
	JDBC,
	/**
	 * Mongo connector.
	 */
	MONGO,
	/**
	 * No operation connector is used for testing purposes.
	 */
	NOOP,
	/**
	 * This connector connects to local moskito instance.
	 */
	LOCALMOSKITO,
	/**
	 * This connector connects to a separate moskito-control instance and proxies everything.
	 */
	CONTROL;
}
