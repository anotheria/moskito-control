package org.moskito.control.ui.resource;

import org.moskito.control.ui.bean.ChartPointBean;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * A bean that represents a single chart.
 *
 * @author lrosenberg
 * @since 10.08.13 00:50
 */
@XmlRootElement
public class ChartBean implements ChartResponseBean{
	/**
	 * Name of the chart.
	 */
	@XmlElement
	private String name;

	/**
	 * Points for this charts.
	 */
	@XmlElement
	private List<ChartPointBean> points;

	/**
	 * Names of the lines.
	 */
	@XmlElement
	private List<String> lineNames = new ArrayList<String>();

	/**
	 * Legend that explains a bit about this chart.
	 */
	@XmlElement
	private String legend;


	public ChartBean(){

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ChartPointBean> getPoints() {
		return points;
	}

	public void setPoints(List<ChartPointBean> points) {
		this.points = points;
	}

	public List<String> getLineNames() {
		return lineNames;
	}

	public void setLineNames(List<String> lineNames) {
		this.lineNames = lineNames;
	}

	public String getLegend() {
		return legend;
	}

	public void setLegend(String legend) {
		this.legend = legend;
	}
}
