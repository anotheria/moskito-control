package org.moskito.control.ui.resource.connector;

import org.moskito.control.ui.resource.ControlReplyObject;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * REST reply object containing requested
 * connector configuration for single component.
 *
 * @author strel
 */
@XmlRootElement
public class ConnectorConfigurationRestResponse extends ControlReplyObject {

	/**
	 * Component's {@link ConnectorConfigurationBean}.
	 */
	@XmlElement()
	private ConnectorConfigurationBean connectorConfiguration;


	public ConnectorConfigurationBean getConnectorConfiguration() {
		return connectorConfiguration;
	}

	public void setConnectorConfiguration(ConnectorConfigurationBean connectorConfiguration) {
		this.connectorConfiguration = connectorConfiguration;
	}
}
