package org.moskito.control.ui.resource.connector;

import org.moskito.control.ui.resource.ControlReplyObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * REST reply object containing requested connector for
 * single component.
 *
 * @author strel
 */
@XmlRootElement
public class ConnectorRestResponse extends ControlReplyObject {

	/**
	 * Component's {@link ConnectorBean}.
	 */
	@XmlElement()
	private ConnectorBean connector;


	public ConnectorBean getConnector() {
		return connector;
	}

	public void setConnector(ConnectorBean connector) {
		this.connector = connector;
	}
}
