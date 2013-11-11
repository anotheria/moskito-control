package org.moskito.control.ui.resource;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 23.08.13 09:20
 */
@XmlRootElement
public class ChartLinesBean implements ChartResponseBean{
	/**
	 * Name of the chart.
	 */
	@XmlElement
	private String name;

	@XmlElement
	private List<ChartLineBean> lines;

	@XmlElement
	private List<String> captions;

	@XmlElement
	private List<Long> timestamps;

	public ChartLinesBean(){
		lines = new LinkedList<ChartLineBean>();
		captions = new LinkedList<String>();
		timestamps = new LinkedList<Long>();
	}

	public void addChartLineBean(ChartLineBean chartLineBean){
		lines.add(chartLineBean);
	}

	public void addCaption(String caption){
		captions.add(caption);
	}

	public void addTimestamp(long aTimestamp){
		timestamps.add(aTimestamp);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ChartLineBean> getLines() {
		return lines;
	}

	public void setLines(List<ChartLineBean> lines) {
		this.lines = lines;
	}

	public List<String> getCaptions() {
		return captions;
	}

	public void setCaptions(List<String> captions) {
		this.captions = captions;
	}
}
