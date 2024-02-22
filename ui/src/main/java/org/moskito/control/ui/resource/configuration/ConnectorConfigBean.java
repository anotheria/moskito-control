package org.moskito.control.ui.resource.configuration;

import org.moskito.control.config.ConnectorType;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Represents a single configured connector.
 */
@XmlRootElement
public class ConnectorConfigBean {
	/**
	 * Type of the connector. Used by the factory to create an instance.
	 */
	@XmlElement
	private ConnectorType type;

	/**
	 * Clazzname of the connector implementation. This class will be instantiated by the factory.
	 */
	@XmlElement
	private String className;


	public ConnectorType getType() {
		return type;
	}

	public void setType(ConnectorType type) {
		this.type = type;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
