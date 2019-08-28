package org.moskito.control.core;

import net.anotheria.util.StringUtils;
import org.configureme.sources.ConfigurationSource;
import org.configureme.sources.ConfigurationSourceKey;
import org.configureme.sources.ConfigurationSourceListener;
import org.configureme.sources.ConfigurationSourceRegistry;
import org.moskito.control.config.ChartConfig;
import org.moskito.control.config.ChartLineConfig;
import org.moskito.control.config.ComponentConfig;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.config.ViewConfig;
import org.moskito.control.config.datarepository.WidgetConfig;
import org.moskito.control.core.chart.Chart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Manages applications.
 *
 * @author lrosenberg
 * @since 01.04.13 23:08
 */
public final class ComponentRepository {

	/**
	 * Map with currently configured components.
	 */
	private ConcurrentMap<String, Component> components;

	/**
	 * Map with configured charts.
	 */
	private ConcurrentMap<String, Chart> charts;

	private ConcurrentMap<String, View> views;

	private ConcurrentMap<String, DataWidget> widgets;

	/**
	 * Manages components events
	 */
	private EventsDispatcher eventsDispatcher = new EventsDispatcher();

	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(ComponentRepository.class);

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
	 * Returns the singleton instance of the ApplicationRepository.
	 * @return instance of the ApplicationRepository
	 */
	public static final ComponentRepository getInstance(){
		return ComponentRepositoryInstanceHolder.instance;
	}

	/**
	 * Creates a new repository.
	 */
	private ComponentRepository(){
		components = new ConcurrentHashMap<>();
		views = new ConcurrentHashMap<>();
		charts = new ConcurrentHashMap<>();
		widgets = new ConcurrentHashMap<>();

		readConfig();

        //Listen for config updates
        final ConfigurationSourceKey sourceKey = new ConfigurationSourceKey(ConfigurationSourceKey.Type.FILE, ConfigurationSourceKey.Format.JSON, "moskitocontrol");
        ConfigurationSourceRegistry.INSTANCE.addListener(sourceKey, new ConfigurationSourceListener() {
            public void configurationSourceUpdated(ConfigurationSource target) {
                log.info("Configuration file updated, reading config...");
                readConfig();
            }
        });
	}

    /**
     * Read applications configuration.
     */
	private void readConfig(){
        components.clear();
		MoskitoControlConfiguration configuration = MoskitoControlConfiguration.getConfiguration();
		ComponentConfig[] configuredComponents = configuration.getComponents();

		if (configuredComponents != null) {
			for (ComponentConfig cc : configuredComponents) {
				Component comp = new Component(cc);
				addComponent(comp);
			}
		}

			//TODO add data widgets
			//app.setWidgets(ac.getDataWidgets());

		ChartConfig[] configuredCharts = configuration.getCharts();
		if (configuredCharts!=null && configuredCharts.length>0){
			for (ChartConfig cc : configuredCharts){
				Chart chart = new Chart(cc.getName(), cc.getLimit());
				ChartLineConfig[] lines = cc.getLines();

				for (ChartLineConfig line : lines){
					for (String componentName : line.getComponentsMatcher().getMatchedComponents(configuredComponents))
						chart.addLine(componentName, line.getAccumulator(), line.getCaption(componentName));
				}


				if (cc.getTags()!=null)
					chart.setTags(Arrays.asList(StringUtils.tokenize(cc.getTags(), ',')));
				addChart(chart);
			}
		}

		WidgetConfig[] configuredWidgets = configuration.getDataprocessing().getWidgets();
		if (configuredWidgets!=null && configuredWidgets.length>0){
			for (WidgetConfig widgetConfig : configuredWidgets){
				DataWidget widget = new DataWidget(widgetConfig);
				addDataWidget(widget);
			}
		}

		ViewConfig[] configuredViews = configuration.getViews();
		if (configuredViews == null ||configuredViews.length==0){
			//TODO add handling to add a default view.
			View defaultView = new View("ALL");
			views.put("ALL", defaultView);
		}else{
			for (ViewConfig vc : configuredViews){
				View v = new View(vc.getName());
				v.setComponentCategoryFilter(vc.getComponentCategories());
				v.setComponentFilter(vc.getComponents());
				v.setComponentTagsFilter(vc.getComponentTags());
				v.setChartFilter(vc.getCharts());
				v.setChartTagsFilter(vc.getChartTags());
				views.put(v.getName(), v);
			}
			//TODO for testing.
			View defaultView = new View("ALL");
			views.put("ALL", defaultView);

		}
	}

	public List<View> getViews(){
		LinkedList<View> viewLinkedList = new LinkedList<>();
		viewLinkedList.addAll(views.values());
		return viewLinkedList;
	}

	public List<Component> getComponents(){
		LinkedList<Component> componentsList = new LinkedList<>();
		componentsList.addAll(components.values());
		return componentsList;
	}

	public List<DataWidget> getDataWidgets(){
		LinkedList<DataWidget> ret = new LinkedList<>();
		ret.addAll(widgets.values());
		return ret;
	}

	public Component getComponent(String componentName){
		return components.get(componentName);
	}

	private void addComponent(Component component){
		components.put(component.getName(), component);
	}

	private void addChart(Chart chart){
		charts.put(chart.getName(), chart);
	}

	private void addDataWidget(DataWidget widget){
		widgets.put(widget.getName(), widget);
	}

	public EventsDispatcher getEventsDispatcher() {
		return eventsDispatcher;
	}

	public View getView(String name) {
		return views.get(name);
	}

	public List<Chart> getCharts() {
		LinkedList<Chart> ret = new LinkedList<>();
		ret.addAll(charts.values());
		return ret;
	}

	/**
	 * Singleton instance holder class.
	 */
	private static class ComponentRepositoryInstanceHolder{
		/**
		 * Singleton instance.
		 */
		private static final ComponentRepository instance = new ComponentRepository();
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

	/**
	 * Returns the worst status of an application component, which is the worst status of the application.
	 * @return worst status of this application
	 */
	public HealthColor getWorstHealthStatus() {
		HealthColor ret = HealthColor.GREEN;
		for (Component c : getComponents()){ //TODO revisit - iterate directly over hashmap
			if (c.getHealthColor().isWorse(ret))
				ret = c.getHealthColor();
		}
		return ret;
	}

	public HealthColor getWorstHealthStatus(List<Component> components) {
		HealthColor ret = HealthColor.GREEN;
		for (Component c : components){ 
			if (c.getHealthColor().isWorse(ret))
				ret = c.getHealthColor();
		}
		return ret;
	}
}
