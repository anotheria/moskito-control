package org.moskito.control.ui.resource;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * This beans contains currently configured views with their components and states.
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
	private List<ViewContainerBean> views = new ArrayList<ViewContainerBean>();

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

	public List<ViewContainerBean> getViews() {
		return views ;
	}

	public void setApplications(List<ViewContainerBean> views) {
		this.views = views;
	}

	/**
	 * Adds a view.
	 * @param viewContainerBean
	 */
	public void addView(ViewContainerBean viewContainerBean){
		views.add(viewContainerBean);
	}
}
