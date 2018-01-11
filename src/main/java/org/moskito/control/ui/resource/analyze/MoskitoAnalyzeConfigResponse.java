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

	@XmlElement
	private String[] components;


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String[] getComponents() {
		return components;
	}

	public void setComponents(String[] components) {
		this.components = components;
	}
}
