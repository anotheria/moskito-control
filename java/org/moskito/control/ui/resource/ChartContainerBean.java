package org.moskito.control.ui.resource;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 09.08.13 15:57
 */
@XmlRootElement
public class ChartContainerBean extends ControlReplyObject{
	@XmlElement
	private List<ChartBean> charts;

	public List<ChartBean> getCharts() {
		return charts;
	}

	public void setCharts(List<ChartBean> charts) {
		this.charts = charts;
	}
}
