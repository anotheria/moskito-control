package org.moskito.control.config;

import com.google.gson.annotations.SerializedName;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

/**
 * Configuration of the application.
 *
 * @author lrosenberg
 * @since 26.02.13 01:33
 */
@ConfigureMe
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2", "EI_EXPOSE_REP"}, justification = "This is the way configureMe works, it provides beans for access")
public class ApplicationConfig {
	/**
	 * Name of the application.
	 */
	@Configure
	@SerializedName("name")
	private String name;

	/**
	 * Components.
	 */

	@Configure
	@SerializedName("@components")
	private ComponentConfig[] components;

	/**
	 * Charts.
	 */
	@Configure
	@SerializedName("@charts")
	private ChartConfig[] charts;

	/**
	 * Data widgets for this application. This should include data widgets configured in DataProcessingConfig.
	 * You can use "[*]" as an element to include all widgets.
	 */
	@Configure
	@SerializedName("dataWidgets")
	private String[] dataWidgets;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ComponentConfig[] getComponents() {
		return components;
	}

	public void setComponents(ComponentConfig[] components) {
		this.components = components;
	}

	public ComponentConfig getComponent(String aName){
		for (ComponentConfig c : components){
			if (c.getName().equals(aName))
				return c;
		}
		throw new IllegalArgumentException("Component with name "+aName+" not found");
	}


	public ChartConfig[] getCharts() {
		return charts;
	}

	public void setCharts(ChartConfig[] charts) {
		this.charts = charts;
	}

	public String[] getDataWidgets() {
		return dataWidgets;
	}

	public void setDataWidgets(String[] dataWidgets) {
		this.dataWidgets = dataWidgets;
	}
}
