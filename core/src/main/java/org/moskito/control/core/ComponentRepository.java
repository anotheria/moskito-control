package org.moskito.control.core;

import net.anotheria.util.StringUtils;
import org.configureme.sources.ConfigurationSource;
import org.configureme.sources.ConfigurationSourceKey;
import org.configureme.sources.ConfigurationSourceListener;
import org.configureme.sources.ConfigurationSourceRegistry;
import org.moskito.control.common.HealthColor;
import org.moskito.control.config.ActionConfig;
import org.moskito.control.config.ChartConfig;
import org.moskito.control.config.ChartLineConfig;
import org.moskito.control.config.ComponentConfig;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.config.ViewConfig;
import org.moskito.control.config.custom.CustomConfigurationProvider;
import org.moskito.control.config.datarepository.WidgetConfig;
import org.moskito.control.core.action.ComponentAction;
import org.moskito.control.core.chart.Chart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

/**
 * Manages components.
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
     * List with configured charts (list keeps order of configuration).
     */
    private final LinkedList<Chart> charts;

	/**
	 * Map of component views. Name -> View.
	 */
	private final ConcurrentMap<String, View> views;

	/**
	 * List of configured widgets.
	 */
    private final List<DataWidget> widgets;

	/**
	 * Map of component actions.
	 */
	private final ConcurrentMap<String, List<ComponentAction>> componentActions;

    /**
     * Manages components events
     */
    private final EventsDispatcher eventsDispatcher = new EventsDispatcher();

    /**
     * Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(ComponentRepository.class);

    /**
     * Timestamp of the last status update.
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
	 * Custom configuration providers allow plugins to modify configuration.
	 */
	private List<CustomConfigurationProvider> customConfigurationProviders = new CopyOnWriteArrayList<>();

    /**
     * Returns the singleton instance of the ApplicationRepository.
     *
     * @return instance of the ApplicationRepository
     */
    public static ComponentRepository getInstance() {
        return ComponentRepositoryInstanceHolder.instance;
    }

    /**
     * Creates a new repository.
     */
    private ComponentRepository() {
        components = new ConcurrentHashMap<>();
        views = new ConcurrentHashMap<>();
        charts = new LinkedList<>();
        widgets = new LinkedList<>();
        componentActions = new ConcurrentHashMap<>();
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
    private void readConfig() {
        components.clear();
        widgets.clear();
        charts.clear();
        componentActions.clear();

        MoskitoControlConfiguration configuration = MoskitoControlConfiguration.getConfiguration();
        ComponentConfig[] configuredComponents = getConfiguredComponents(configuration);

        if (configuredComponents != null) {
            for (ComponentConfig cc : configuredComponents) {
                Component comp = new Component(cc);
                addComponent(comp);
            }
        }

        ActionConfig[] componentActions = configuration.getActions();
        if (componentActions != null) {
            for (ActionConfig ac : componentActions) {
                ComponentAction action = new ComponentAction(ac);
                addComponentAction(action);
            }
        }

        ChartConfig[] configuredCharts = configuration.getCharts();
        //System.out.println("Reading charts config "+Arrays.toString(configuredCharts));
        if (configuredCharts != null && configuredCharts.length > 0) {
            for (ChartConfig cc : configuredCharts) {
                Chart chart = new Chart(cc.getName(), cc.getLimit());
                ChartLineConfig[] lines = cc.getLines();

                for (ChartLineConfig line : lines) {
                    try {
                        ChartLineComponentMatcher clcm = new ChartLineComponentMatcher(line);
                        String[] componentNamesForThisChart = clcm.getMatchedComponents(components.values());
                        for (String componentName : componentNamesForThisChart)
                            chart.addLine(componentName, line.getAccumulator(), line.getCaption(componentName));
                    }catch(IllegalArgumentException e){
                        log.error("Can't add chart line "+line+", skipping it in chat "+cc.getName(), e);
                    }
                }


                if (cc.getTags() != null)
                    chart.setTags(Arrays.asList(StringUtils.tokenize(cc.getTags(), ',')));
                addChart(chart);
            }
        }

        WidgetConfig[] configuredWidgets = getConfiguredWidgets(configuration);
        if (configuredWidgets != null && configuredWidgets.length > 0) {
            for (WidgetConfig widgetConfig : configuredWidgets) {
                DataWidget widget = new DataWidget(widgetConfig);
                addDataWidget(widget);
            }
        }

        ViewConfig[] configuredViews = configuration.getViews();
        if (configuredViews == null || configuredViews.length == 0) {
            //If no views are configured, we create a view automatically.
            View defaultView = new View("ALL");
            views.put("ALL", defaultView);
        } else {
            if (configuration.isEnableAllView()) {
                View defaultView = new View("ALL");
                views.put("ALL", defaultView);
            }
            for (ViewConfig vc : configuredViews) {
                View v = new View(vc.getName());
                v.setComponentCategoryFilter(vc.getComponentCategories());
                v.setComponentFilter(vc.getComponents());
                v.setComponentTagsFilter(vc.getComponentTags());
                v.setChartFilter(vc.getCharts());
                v.setChartTagsFilter(vc.getChartTags());
                v.setWidgetsFilter(vc.getWidgets());
                v.setWidgetTagsFilter(vc.getWidgetTags());
                views.put(v.getName(), v);
            }

        }
    }

	private WidgetConfig[] getConfiguredWidgets(MoskitoControlConfiguration configuration) {
    	if (noCustomConfigurationProviders())
    		return configuration.getDataprocessing().getWidgets();

    	List<WidgetConfig> temporaryList = new ArrayList<>();
    	temporaryList.addAll(Arrays.asList(configuration.getDataprocessing().getWidgets()));
		for (CustomConfigurationProvider ccp : customConfigurationProviders){
			if (ccp.getDataProcessingConfig().getWidgets()!=null && ccp.getDataProcessingConfig().getWidgets().length>0)
				temporaryList.addAll(Arrays.asList(ccp.getDataProcessingConfig().getWidgets()));
		}
    	return temporaryList.toArray(new WidgetConfig[temporaryList.size()]);
	}

	private ComponentConfig[] getConfiguredComponents(MoskitoControlConfiguration configuration){
    	//System.out.println("Custom cp check no?"+noCustomConfigurationProviders());
		if (noCustomConfigurationProviders())
			return configuration.getComponents();

		List<ComponentConfig> temporaryList = new ArrayList<>();
		temporaryList.addAll(Arrays.asList(configuration.getComponents()));
		for (CustomConfigurationProvider ccp : customConfigurationProviders){
			//System.out.println("Now "+temporaryList.size() + " elements.");
			if (ccp.getComponents()!=null) {
				//System.out.println("adding "+ccp.getComponents());
				temporaryList.addAll(ccp.getComponents());
			}
		}
		return temporaryList.toArray(new ComponentConfig[temporaryList.size()]);

    }

    private boolean noCustomConfigurationProviders(){
    	return customConfigurationProviders==null || customConfigurationProviders.size()==0;
	}

	public List<View> getViews() {
        return new LinkedList<>(views.values());
    }

    public List<Component> getComponents() {
        return new LinkedList<>(components.values());
    }

    public List<DataWidget> getDataWidgets() {
        return widgets;
    }

    public Component getComponent(String componentName) {
        return components.get(componentName);
    }

    public void addComponent(Component component) {
        components.put(component.getName(), component);
    }

    public void addComponentAction(ComponentAction action) {
        String component = action.getComponentName();
        if (!componentActions.containsKey(component)) {
            componentActions.put(component, new ArrayList<>());
        }
        componentActions.get(component).add(action);
    }

    public List<ComponentAction> getComponentActions(String componentName) {
        return componentActions.get(componentName);
    }

    public ComponentAction getComponentAction(String componentName, String actionName) {
        List<ComponentAction> componentActions = this.componentActions.get(componentName);
        if (componentActions == null || componentActions.isEmpty()) {
            return null;
        }
        for (ComponentAction action : componentActions) {
            if (action.getName().equals(actionName)) {
                return action;
            }
        }
        return null;
    }

    public void setComponents(ConcurrentMap<String, Component> components) {
        this.components = components;
    }

    private void addChart(Chart chart) {
        charts.add(chart);
    }

    private void addDataWidget(DataWidget widget) {
        widgets.add(widget);
    }

    public EventsDispatcher getEventsDispatcher() {
        return eventsDispatcher;
    }

    public View getView(String name) {
        if (name == null)
            return views.values().iterator().next();//return first available view.
        return views.get(name);
    }

    public List<Chart> getCharts() {
        return charts;
    }


    /**
     * Singleton instance holder class.
     */
    private static class ComponentRepositoryInstanceHolder {
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
     *
     * @return worst status of this application
     */
    public HealthColor getWorstHealthStatus() {
        HealthColor ret = HealthColor.GREEN;
        for (Component c : getComponents()) { //TODO revisit - iterate directly over hashmap
            if (c.getHealthColor().isWorse(ret))
                ret = c.getHealthColor();
        }
        return ret;
    }

    public HealthColor getWorstHealthStatus(List<Component> components) {
        HealthColor ret = HealthColor.GREEN;
        for (Component c : components) {
            if (c.getHealthColor().isWorse(ret))
                ret = c.getHealthColor();
        }
        return ret;
    }

	/**
	 * Adds a custom provider. To be used by a plugin.
	 */
	public void addCustomConfigurationProvider(CustomConfigurationProvider provider){
		customConfigurationProviders.add(provider);
		readConfig();
	}

	/**
	 * Helper class to finding components chart line
	 * by pattern specified in chart line config
	 */
	public class ChartLineComponentMatcher {

		/**
		 * Name of component category
		 * Only components with this category will match
		 * If null - all categories is match
		 */
		private String categoryName;
		/**
		 * Pattern for component name
		 */
		private Pattern namePattern;

		private Set<String> componentTagsSet = Collections.emptySet();

		/**
		 * Parses component name pattern string
		 */
		private ChartLineComponentMatcher(ChartLineConfig chartLineConfig){
			String componentTags = chartLineConfig.getComponentTags();
			String componentName = chartLineConfig.getComponent();


			if (componentTags!=null && componentTags.length()>0){
				String[] tagsArray = StringUtils.tokenize(componentTags, ',');
				componentTagsSet = new HashSet<>();
				for (String t: tagsArray){
					componentTagsSet.add(t);
				}
			}

			String componentNamePatternString; // Part of pattern string belongs to component name

            if (componentName== null || componentName.length() == 0)
                throw new IllegalArgumentException("Component name is empty "+chartLineConfig);
			if(componentName.contains(":")){ // Means component category specified in config

				// Split pattern string to category (0 index) and name (1 index)
				String[] splittedPatternStrings = componentName.split(":");
				categoryName = splittedPatternStrings[0].trim();
				componentNamePatternString = splittedPatternStrings[1].trim();

			}
			else
				// All pattern string belongs to component name. Any category will match
				componentNamePatternString = componentName;

			// Pattern to match name string
			String componentPattern = "";

			// Adding zero or more symbols to begin of regexp if there is asterisks in beginning
			if(componentNamePatternString.charAt(0) == '*') {
				componentPattern = ".*";
				componentNamePatternString = componentNamePatternString.substring(1); // removing asterisks
			}

			// Check length in case initial pattern string contains only one asterisks symbol
			if(componentNamePatternString.length() > 0)

				// Adding zero or more symbols to end of regexp if there is asterisks on end
				if(componentNamePatternString.charAt(componentNamePatternString.length() - 1) == '*'){

					// Check for cases there is only two asterisks in pattern string
					if(componentNamePatternString.length() > 0)
						// Quoting component name string without asterisks
						componentPattern += Pattern.quote(
								componentNamePatternString.substring(
										0,
										componentNamePatternString.length() - 1
								)
						);

					// Adding zero or more symbols to end of regexp
					componentPattern += ".*";

				}
				else
					// No asterisks on end of pattern. Quoting component name string
					componentPattern += Pattern.quote(componentNamePatternString);

			namePattern = Pattern.compile(componentPattern);

		}

		/**
		 * Check is component name and category matches to this pattern.
		 * @param categoryName component category name
		 * @param componentName component name
		 * @return true  - component matches
		 * 		   false - no
		 */
		private boolean isComponentMatches(String categoryName, String componentName, List<String> tags){
			// Category name is not specified or matches to category in arguments
			return (this.categoryName == null || this.categoryName.equals(categoryName)) &&
					// And component name matches to pattern
					(namePattern.matcher(componentName).find());

		}


		/**
		 * Finds components names with name and category matches to this pattern
		 * from components config array
		 * @param components components to find matches
		 * @return array of matched components names
		 */

		public String[] getMatchedComponents(Collection<Component> components){

			List<String> matchesComponents = new ArrayList<>();

			for(Component c : components){

				//if there are no tags in this chart, proceed with name matching, old logic.
				if (componentTagsSet.isEmpty()) {
					if (isComponentMatches(c.getCategory(), c.getName(), c.getTags()))
						matchesComponents.add(c.getName());
				}else{
					//so tasks are required
					//if component has no tags skip it.
					if (c.getTags().size()==0){
						continue;
					}

					//check if component has a matching tag && name matches.
					boolean tagMatched = false;
					for (String tag : c.getTags()) {
						if (componentTagsSet.contains(tag))
							tagMatched = true;

					}
					if (tagMatched==true && isComponentMatches(c.getCategory(), c.getName(), c.getTags())){
							matchesComponents.add(c.getName());
					}

				}

			}

			return matchesComponents.toArray(new String[matchesComponents.size()]);

		}

		public String getCategoryName() {
			return categoryName;
		}

		public Pattern getNamePattern() {
			return namePattern;
		}
	}

    public void removeComponent(String name){
        components.remove(name);
    }

    public void removeChart(String name){
        for (Chart chart: charts){
            if (chart.getName().equals(name)){
                charts.remove(chart);
                break;
            }
        }
    }

    public void removeView(String name) {
        views.remove(name);
    }

}
