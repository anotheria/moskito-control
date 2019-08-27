package org.moskito.control.core;

import org.moskito.control.core.chart.Chart;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents an application at runtime.
 *
 * @author lrosenberg
 * @since 26.02.13 01:32
 */
@Deprecated
public class Application implements Comparable<Application>{

    /**
	 * Name of the application.
	 */
	private String name;

    /**
	 * Components which are part of the application.
	 */
	private List<Component> components = new LinkedList<Component>();

	/**
	 * Charts associated with this application.
	 */
	private List<Chart> charts = new LinkedList<Chart>();

	private String[] widgets;


    /**
	 * Creates a new application.
	 */
	public Application(){

	}

	/**
	 * Creates a new application.
	 */
	public Application(String name){
		this.name = name;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public void addComponent(Component c){
		components.add(c);
	}

	public List<Component> getComponents() {
		return components;
	}

	/**
	 * Returns a component by its name.
	 * @param aName name of the component.
	 * @return component with name correspond to method argument
	 */
	public Component getComponent(String aName){
		for (Component c : components)
			if (c.getName().equals(aName))
				return c;
		throw new IllegalArgumentException("Component "+aName+" is not known");
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


	public String[] getWidgets() {
		return widgets;
	}

	public void setWidgets(String[] widgets) {
		this.widgets = widgets;
	}

	@Override
	public int compareTo(Application o) {
		return name.compareTo(o.getName());
	}

	@Override
	public boolean equals(Object o){
		return o instanceof Application && name.equals(((Application)o).getName());
	}

	@Override public String toString(){
		return name;
	}

	@Override
	public int hashCode(){
		return name.hashCode();
	}

}