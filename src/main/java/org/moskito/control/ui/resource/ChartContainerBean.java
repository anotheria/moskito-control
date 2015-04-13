package org.moskito.control.ui.resource;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Container bean as reply object for REST API Call for charts.
 *
 * @author lrosenberg
 * @since 09.08.13 15:57
 */
@XmlRootElement
public class ChartContainerBean extends ControlReplyObject{
	/**
	 * Contained charts.
	 */
	@XmlElement
	private List<ChartResponseBean> charts;

	public List<ChartResponseBean> getCharts() {
		return charts;
	}

	public void setCharts(List<ChartResponseBean> charts) {
		this.charts = charts;
	}
}
