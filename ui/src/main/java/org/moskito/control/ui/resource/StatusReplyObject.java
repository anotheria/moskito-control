package org.moskito.control.ui.resource;

import org.moskito.control.core.updater.UpdaterStatus;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

/**
 * The reply container object for the StatusResource.
 *
 * @author lrosenberg
 * @since 12.08.13 18:22
 */
@XmlRootElement
public class StatusReplyObject extends ControlReplyObject{
	/**
	 * The statuses map for the status of each application.
	 */
	@XmlElement
	private Map<String, ViewStatusBean> statuses = new HashMap<String, ViewStatusBean>();

	/**
	 * The map for updater status for all updaters.
	 */
	@XmlElement
	private Map<String, UpdaterStatus> updaterStatuses = new HashMap<String, UpdaterStatus>();

	public void addStatus(String applicationName, ViewStatusBean bean){
		statuses.put(applicationName, bean);
	}

	public Map<String, ViewStatusBean> getStatuses() {
		return statuses;
	}

	public void setStatuses(Map<String, ViewStatusBean> statuses) {
		this.statuses = statuses;
	}

	public Map<String, UpdaterStatus> getUpdaterStatuses() {
		return updaterStatuses;
	}

	public void setUpdaterStatuses(Map<String, UpdaterStatus> updaterStatuses) {
		this.updaterStatuses = updaterStatuses;
	}

	public void addUpdaterStatus(String updaterName, UpdaterStatus status){
		updaterStatuses.put(updaterName, status);
	}
}
