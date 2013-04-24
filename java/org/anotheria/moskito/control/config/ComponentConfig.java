package org.anotheria.moskito.control.config;

import org.anotheria.moskito.control.connectors.ConnectorType;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

/**
 * Configuration of a component.
 *
 * @author lrosenberg
 * @since 26.02.13 01:33
 */
@ConfigureMe
public class ComponentConfig {

	/**
	 * Name of the component.
	 */
	@Configure
	private String name;

	/**
	 * Category.
	 */
	@Configure
	private String category;

	/**
	 * Type of the connector for this component.
	 */
	@Configure
	private ConnectorType connectorType;

	/**
	 * Connector specific location.
	 */
	@Configure
	private String location;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public ConnectorType getConnectorType() {
		return connectorType;
	}

	public void setConnectorType(ConnectorType connectorType) {
		this.connectorType = connectorType;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
