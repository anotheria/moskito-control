package org.moskito.control.ui.resource.connector;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * Bean contains connector information.
 *
 * @author strel
 */
@XmlRootElement
public class ConnectorInformationBean {

	/**
	 * Connector specific information as {@link Map}.
	 */
	@XmlElement
	private Map<String, String> info;


	public Map<String, String> getInfo() {
		return info;
	}

	public void setInfo(Map<String, String> info) {
		this.info = info;
	}
}
