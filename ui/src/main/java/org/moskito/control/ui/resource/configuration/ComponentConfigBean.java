package org.moskito.control.ui.resource.configuration;

import org.moskito.control.config.ConnectorType;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Configuration of a component.
 */
@XmlRootElement
public class ComponentConfigBean {

	/**
	 * Name of the component.
	 */
	@XmlElement
	private String name;

	/**
	 * Category.
	 */
	@XmlElement
	private String category;

	/**
	 * Type of the connector for this component.
	 */
	@XmlElement
	private ConnectorType connectorType;

	/**
	 * Connector specific location.
	 */
	@XmlElement
	private String location;

	/**
	 * Connector specific credentials.
	 */
	@XmlElement
	private String credentials;


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

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}
}
