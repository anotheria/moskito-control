package org.moskito.control.ui.resource.analyze;

import org.moskito.control.ui.resource.ControlReplyObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * REST response for general configured MoSKito-Analyze properties.
 * @author strel
 */
@XmlRootElement
public class MoskitoAnalyzeConfigResponse extends ControlReplyObject {

	/**
	 * MoSKito-Analyze application URL.
	 */
	@XmlElement
	private String url;

	/**
	 * Array of hosts.
	 */
	@XmlElement
	private String[] hosts;


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String[] getHosts() {
		return hosts;
	}

	public void setHosts(String[] hosts) {
		this.hosts = hosts;
	}
}
