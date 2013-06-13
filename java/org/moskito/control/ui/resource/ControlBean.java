package org.moskito.control.ui.resource;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 05.06.13 22:25
 */
@XmlRootElement
public class ControlBean extends ControlReplyObject{
	@XmlElement
	private List<ApplicationContainerBean> applications = new ArrayList<ApplicationContainerBean>();

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

	public void addApplication(ApplicationContainerBean applicationContainerBean){
		applications.add(applicationContainerBean);
	}
}
