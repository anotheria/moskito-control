package org.moskito.control.ui.resource;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents the list of values for the line.
 *
 * @author lrosenberg
 * @since 23.08.13 09:21
 */
@XmlRootElement
public class ChartLineBean {
	/**
	 * Name of the line.
	 */
	@XmlElement
	private String lineName;

	/**
	 * Values for this line.
	 */
	private List<String> values;

	public ChartLineBean(){
		lineName = "unnamed";
		values = new LinkedList<String>();
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public void addValue(String aValue){
		values.add(aValue);
	}
}
