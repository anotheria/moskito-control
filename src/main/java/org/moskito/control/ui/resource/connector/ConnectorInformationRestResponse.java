package org.moskito.control.ui.resource.connector;

import org.moskito.control.ui.resource.ControlReplyObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * REST reply object containing requested
 * connector information.
 *
 * @author strel
 */
@XmlRootElement
public class ConnectorInformationRestResponse extends ControlReplyObject {

	/**
	 * Component's {@link ConnectorInformationBean}.
	 */
	@XmlElement()
	private ConnectorInformationBean connectorInformation;


	public ConnectorInformationBean getConnectorInformation() {
		return connectorInformation;
	}

	public void setConnectorInformation(ConnectorInformationBean connectorInformation) {
		this.connectorInformation = connectorInformation;
	}
}
