package org.moskito.control.ui.resource;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * This beans contains currently configured applications with their components and states.
 *
 * @author lrosenberg
 * @since 05.06.13 22:25
 */
@XmlRootElement
public class ControlBean extends ControlReplyObject{
	/**
	 * Configured applications.
	 */
	@XmlElement
	private List<ApplicationContainerBean> applications = new ArrayList<ApplicationContainerBean>();

	/**
	 * Timestamp of last updater run.
	 */
	@XmlElement
	private String timestampOfLastUpdaterRun;

	public String getTimestampOfLastUpdaterRun() {
		return timestampOfLastUpdaterRun;
	}

	public void setTimestampOfLastUpdaterRun(String timestampOfLastUpdaterRun) {
		this.timestampOfLastUpdaterRun = timestampOfLastUpdaterRun;
	}

	public List<ApplicationContainerBean> getApplications() {
		return applications;
	}

	public void setApplications(List<ApplicationContainerBean> applications) {
		this.applications = applications;
	}

	/**
	 * Adds an application.
	 * @param applicationContainerBean
	 */
	public void addApplication(ApplicationContainerBean applicationContainerBean){
		applications.add(applicationContainerBean);
	}
}
