package org.moskito.control.core;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents an application at runtime.
 *
 * @author lrosenberg
 * @since 26.02.13 01:32
 */
public class Application implements Comparable<Application>{
	/**
	 * Name of the application.
	 */
	private String name;
	/**
	 * Components which are part of the application.
	 */
	private List<Component> components = new LinkedList<Component>();

	private List<Chart> charts = new LinkedList<Chart>();

	public Application(){

	}

	public Application(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the worst status of an application component, which is the worst status of the application.
	 * @return
	 */
	public HealthColor getWorstHealthStatus() {
		HealthColor ret = HealthColor.GREEN;
		for (Component c : components){
			if (c.getHealthColor().isWorse(ret))
				ret = c.getHealthColor();
		}
		return ret;
	}

	public void addComponent(Component c){
		components.add(c);
	}

	public List<Component> getComponents() {
		return components;
	}

	public Component getComponent(String name){
		for (Component c : components)
			if (c.getName().equals(name))
				return c;
		throw new IllegalArgumentException("Component "+name+" is not known");
	}

	@Override
	public int compareTo(Application o) {
		return name.compareTo(o.getName());
	}

	@Override public String toString(){
		return name;
	}

	public List<Chart> getCharts() {
		return charts;
	}

	public void setCharts(List<Chart> charts) {
		this.charts = charts;
	}

	public void addChart(Chart c){
		charts.add(c);
	}
}
