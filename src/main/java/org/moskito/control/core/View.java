package org.moskito.control.core;

import org.moskito.control.core.chart.Chart;

import java.util.LinkedList;
import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 2019-08-27 14:47
 */
public class View {
	private String name;

	public View(String aName){
		name = aName;
	}

	public String getName(){
		return name;
	}

	public List<Component> getComponents() {
		return ComponentRepository.getInstance().getComponents();
	}


	public HealthColor getWorstHealthStatus() {
		return ComponentRepository.getInstance().getWorstHealthStatus();
	}

	public List<Chart> getCharts() {
		//TODO revisit
		return new LinkedList();
	}

	public String[] getWidgets() {
		//TODO revisit
		return new String[0];
	}
}
