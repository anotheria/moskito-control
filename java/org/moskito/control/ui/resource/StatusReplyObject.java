package org.moskito.control.ui.resource;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.08.13 18:22
 */
@XmlRootElement
public class StatusReplyObject extends ControlReplyObject{
	@XmlElement
	private Map<String, ApplicationStatusBean> statuses = new HashMap<String, ApplicationStatusBean>();

	public void addStatus(String applicationName, ApplicationStatusBean bean){
		statuses.put(applicationName, bean);
	}

	public Map<String, ApplicationStatusBean> getStatuses() {
		return statuses;
	}

	public void setStatuses(Map<String, ApplicationStatusBean> statuses) {
		this.statuses = statuses;
	}
}
