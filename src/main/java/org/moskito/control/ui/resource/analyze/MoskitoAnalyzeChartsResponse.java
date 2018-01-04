package org.moskito.control.ui.resource.analyze;

import org.moskito.control.ui.resource.ControlReplyObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * REST response for configured chart parameters.
 * @author strel
 */
@XmlRootElement
public class MoskitoAnalyzeChartsResponse extends ControlReplyObject {

    /**
	 * {@link List} of configured {@link MoskitoAnalyzeChartBean}s.
	 */
	@XmlElement
	private List<MoskitoAnalyzeChartBean> charts;


	public List<MoskitoAnalyzeChartBean> getCharts() {
		return charts;
	}

	public void setCharts(List<MoskitoAnalyzeChartBean> charts) {
		this.charts = charts;
	}
}
