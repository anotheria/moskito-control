package org.moskito.control.ui.resource.accumulators;

import org.moskito.control.ui.bean.ChartBean;
import org.moskito.control.ui.resource.ControlReplyObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a container bean for accumulator chart beans.
 * @author strel
 */
@XmlRootElement
public class AccumulatorChartsListResponse extends ControlReplyObject {

	/**
	 * Accumulator chart beans list.
	 */
	@XmlElement()
	private List<ChartBean> charts;


	public AccumulatorChartsListResponse() {
		this.charts = new ArrayList<>();
	}

	public List<ChartBean> getCharts() {
		return charts;
	}

	public void setCharts(List<ChartBean> charts) {
		this.charts = charts;
	}


}
