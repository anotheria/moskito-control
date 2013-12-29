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

	/**
	 * Timestamp of the last application status update.
	 */
	private long lastStatusUpdaterRun;
	/**
	 * Timestamp of the last chart data update.
	 */
	private long lastChartUpdaterRun;

	/**
	 * Timestamp of the last successful application status update.
	 */
	private long lastStatusUpdaterSuccess;
	/**
	 * Timestamp of the last successful chart data update.
	 */
	private long lastChartUpdaterSuccess;

	/**
	 * Number of the status updater runs.
	 */
	private long statusUpdaterRunCount;
	/**
	 * Number of the chart updater runs.
	 */
	private long chartUpdaterRunCount;

	/**
	 * Number of the successful status updater runs.
	 */
	private long statusUpdaterSuccessCount;
	/**
	 * Number of the successful chart updater runs.
	 */
	private long chartUpdaterSuccessCount;


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

	public long getLastStatusUpdaterRun() {
		return lastStatusUpdaterRun;
    }

    public void setLastStatusUpdaterRun(long lastStatusUpdaterRun) {
        statusUpdaterRunCount++;
        this.lastStatusUpdaterRun = lastStatusUpdaterRun;
    }

	public long getLastChartUpdaterRun() {
		return lastChartUpdaterRun;
	}

    public void setLastChartUpdaterRun(long lastChartUpdaterRun) {
        chartUpdaterRunCount++;
        this.lastChartUpdaterRun = lastChartUpdaterRun;
    }

	public long getLastStatusUpdaterSuccess() {
		return lastStatusUpdaterSuccess;
	}

	public void setLastStatusUpdaterSuccess(long lastStatusUpdaterSuccess) {
		statusUpdaterSuccessCount++;
		this.lastStatusUpdaterSuccess = lastStatusUpdaterSuccess;
	}

	public long getLastChartUpdaterSuccess() {
		return lastChartUpdaterSuccess;
    }

	public void setLastChartUpdaterSuccess(long lastChartUpdaterSuccess) {
		chartUpdaterSuccessCount++;
		this.lastChartUpdaterSuccess = lastChartUpdaterSuccess;
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

	/**
	 * Returns a component by its name.
	 * @param aName name of the component.
	 * @return
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

	public long getStatusUpdaterRunCount() {
		return statusUpdaterRunCount;
	}

	public long getChartUpdaterRunCount() {
		return chartUpdaterRunCount;
	}

	public long getStatusUpdaterSuccessCount() {
		return statusUpdaterSuccessCount;
	}

	public long getChartUpdaterSuccessCount() {
		return chartUpdaterSuccessCount;
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