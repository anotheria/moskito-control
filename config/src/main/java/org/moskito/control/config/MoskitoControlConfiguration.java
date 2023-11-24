package org.moskito.control.config;

import com.google.gson.annotations.SerializedName;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.config.datarepository.DataProcessingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Configuration holder class for MoSKito Control. The configuration of MoSKito control is located in moskitocontrol.json file in the classpath.
 *
 * @author lrosenberg
 * @since 26.02.13 18:50
 */
@ConfigureMe (name="moskitocontrol", allfields = true)
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2", "EI_EXPOSE_REP"}, justification = "This is the way configureMe works, it provides beans for access")
public class MoskitoControlConfiguration {

	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(MoskitoControlConfiguration.class);

	/**
	 * Configured components and their components.
	 */
	@Configure
	@SerializedName("@components")
	private ComponentConfig[] components;

    /**
     * Configured component actions.
     */
    @Configure
    @SerializedName("@actions")
    private ActionConfig[] actions;

	/**
	 * Charts.
	 */
	@Configure
	@SerializedName("@charts")
	private ChartConfig[] charts;

	@Configure
	@SerializedName("@views")
	private ViewConfig[] views;

	/**
	 * Data widgets for this application. This should include data widgets configured in DataProcessingConfig.
	 * You can use "[*]" as an element to include all widgets.
	 */
	@Configure
	@SerializedName("dataWidgets")
	private String[] dataWidgets;


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


	/**
	 * Configured connectors.
	 */
	@Configure
	@SerializedName("@connectors")
	private ConnectorConfig[] connectors = new ConnectorConfig[0];

	/**
	 * Number of elements to keep in the history.
	 */
	@Configure
	@SerializedName("historyItemsAmount")
	private int historyItemsAmount = 500;

	/**
	 * Dynamic component status timeout. If component not responding during timeout - "red" status set.
	 */
	@Configure
	@SerializedName("componentStatusTimeoutInSeconds")
	private long componentStatusTimeoutInSeconds = TimeUnit.MINUTES.toSeconds(5);

    /**
     * Status change notifications muting time/period in minutes.
     */
    @Configure
    @SerializedName("notificationsMutingTime")
    private int notificationsMutingTime;

	/**
	 * Configuration of the status updater. A default configuration is provided, so you don't need to overwrite it,
	 * except for tuning.
	 */
	@Configure
	@SerializedName("@statusUpdater")
	private UpdaterConfig statusUpdater = new UpdaterConfig(10, 60, 10);

	/**
	 * Configuration of the charts updater. A default configuration is provided, so you don't need to overwrite it,
	 * except for tuning.
	 */
	@Configure
	@SerializedName("@chartsUpdater")
	private UpdaterConfig chartsUpdater = new UpdaterConfig(5, 60, 40);

	/**
	 * The view which is shown if no other view is selected.
	 */
	@Configure
	@SerializedName("defaultView")
	private String defaultView;

	/**
	 * If true the 'ALL' view is enabled.
	 */
	@Configure
	@SerializedName("enableAllView")
	private boolean enableAllView = true;

	/**
	 * Config objects for plugins.
	 */
	@Configure
	@SerializedName("@pluginsConfig")
	private PluginsConfig pluginsConfig = new PluginsConfig();

	@Configure
	@SerializedName("@dataprocessing")
	private DataProcessingConfig dataprocessing = new DataProcessingConfig();

	/**
	 * If true, the usage is tracked via pixel.
	 */
	@Configure
	@SerializedName("trackUsage")
	private boolean trackUsage = true;


	/**
	 * Returns the active configuration instance. The configuration object will update itself if the config is changed on disk.
	 * @return configuration instance
	 */
	public static final MoskitoControlConfiguration getConfiguration(){
		return MoskitoControlConfigurationHolder.instance;
	}


	/**
	 * Loads a new configuration object from disk. This method is for unit testing.
	 * @return configuration object
	 */
	public static final MoskitoControlConfiguration loadConfiguration(){
		MoskitoControlConfiguration config = new MoskitoControlConfiguration();
		try{
			ConfigurationManager.INSTANCE.configure(config);
		}catch(IllegalArgumentException e){
			//ignored
		}
		return config;
	}

	public ConnectorConfig[] getConnectors() {
		return connectors;
	}

	public void setConnectors(ConnectorConfig[] connectors) {
		this.connectors = connectors;
	}

	public int getHistoryItemsAmount() {
		return historyItemsAmount;
	}

	public void setHistoryItemsAmount(int historyItemsAmount) {
		this.historyItemsAmount = historyItemsAmount;
	}

	public long getComponentStatusTimeoutInSeconds() {
		return componentStatusTimeoutInSeconds;
	}

	public void setComponentStatusTimeoutInSeconds(long componentStatusTimeoutInSeconds) {
		this.componentStatusTimeoutInSeconds = componentStatusTimeoutInSeconds;
	}

	public int getNotificationsMutingTime() {
        return notificationsMutingTime;
    }

    public void setNotificationsMutingTime(int notificationsMutingTime) {
        this.notificationsMutingTime = notificationsMutingTime;
    }

    public UpdaterConfig getStatusUpdater() {
		return statusUpdater;
	}

	public void setStatusUpdater(UpdaterConfig statusUpdater) {
		this.statusUpdater = statusUpdater;
	}

	public UpdaterConfig getChartsUpdater() {
		return chartsUpdater;
	}

	public void setChartsUpdater(UpdaterConfig chartsUpdater) {
		this.chartsUpdater = chartsUpdater;
	}



	/**
	 * Holder class for singleton instance.
	 */
	private static class MoskitoControlConfigurationHolder{
		/**
		 * Singleton instance of the MoskitoControlConfiguration object.
		 */
		static final MoskitoControlConfiguration instance;
		static{
			instance = new MoskitoControlConfiguration();
			try {
				ConfigurationManager.INSTANCE.configure(instance);
			}catch(IllegalArgumentException e){
				log.warn("can't find configuration - ensure you have moskitocontrol.json in the classpath");
			}
		}
	}

	public PluginsConfig getPluginsConfig() {
		return pluginsConfig;
	}

	public void setPluginsConfig(PluginsConfig pluginsConfig) {
		this.pluginsConfig = pluginsConfig;
	}

	public boolean isTrackUsage() {
		return trackUsage;
	}

	public void setTrackUsage(boolean trackUsage) {
		this.trackUsage = trackUsage;
	}

	public DataProcessingConfig getDataprocessing() {
		return dataprocessing;
	}

	public void setDataprocessing(DataProcessingConfig dataprocessing) {
		this.dataprocessing = dataprocessing;
	}

	public ComponentConfig[] getComponents() {
		return components;
	}

	public void setComponents(ComponentConfig[] components) {
		this.components = components;
	}

	public ViewConfig[] getViews() {
		return views;
	}

	public void setViews(ViewConfig[] views) {
		this.views = views;
	}

	public String getDefaultView() {
		return defaultView;
	}

	public void setDefaultView(String defaultView) {
		this.defaultView = defaultView;
	}
	public boolean isEnableAllView() {
		return enableAllView;
	}

	public void setEnableAllView(boolean enableAllView) {
		this.enableAllView = enableAllView;
	}

    public ActionConfig[] getActions() {
        return actions;
    }

    public void setActions(ActionConfig[] actions) {
        this.actions = actions;
    }

    @Override
    public String toString() {
        return "MoskitoControlConfiguration{" +
                "components=" + Arrays.toString(components) +
                ", actions=" + Arrays.toString(actions) +
                ", charts=" + Arrays.toString(charts) +
                ", views=" + Arrays.toString(views) +
                ", dataWidgets=" + Arrays.toString(dataWidgets) +
                ", connectors=" + Arrays.toString(connectors) +
                ", historyItemsAmount=" + historyItemsAmount +
                ", componentStatusTimeoutInSeconds=" + componentStatusTimeoutInSeconds +
                ", notificationsMutingTime=" + notificationsMutingTime +
                ", statusUpdater=" + statusUpdater +
                ", chartsUpdater=" + chartsUpdater +
                ", defaultView='" + defaultView + '\'' +
                ", enableAllView=" + enableAllView +
                ", pluginsConfig=" + pluginsConfig +
                ", dataprocessing=" + dataprocessing +
                ", trackUsage=" + trackUsage +
                '}';
    }

	//Operations for the new endpoint to allow config manipulation via REST

	public void addComponent(ComponentConfig newComponent){
		//first we check if we have a component by this name, if positive - update, if negative - create.
		int i = 0;
		for (ComponentConfig component : components){
			if (component.getName().equals(newComponent.getName())){
				components[i] = newComponent;
				return;
			}
			i++;
		}

		ComponentConfig[] newComponents = Arrays.copyOf(components, components.length + 1);

		newComponents[newComponents.length - 1] = newComponent;
		components = newComponents;
	}

	public void removeComponent(String componentName){
		//first we check if we have a component by this name, otherwise this method could produce an exception.
		boolean hasComponent = false;
		for (ComponentConfig component : components){
			if (component.getName().equals(componentName)){
				hasComponent = true;
				break;
			}
		}
		if (!hasComponent)
			return ;

		ComponentConfig[] newComponents = new ComponentConfig[components.length-1];
		int i = 0;
		for (ComponentConfig component : components){
			if (!component.getName().equals(componentName)){
				newComponents[i] = component;
				i++;
			}
		}
		components = newComponents;
	}

	public void removeChart(String name) {
		boolean hasChart = false;
		for (ChartConfig chart : charts){
			if (chart.getName().equals(name)){
				hasChart = true;
				break;
			}
		}
		if (!hasChart)
			return ;

		ChartConfig[] newCharts = new ChartConfig[charts.length-1];
		int i = 0;
		for (ChartConfig chart : charts){
			if (!chart.getName().equals(name)){
				newCharts[i] = chart;
				i++;
			}
		}
		charts = newCharts;
	}

	public void removeView(String name) {
		boolean hasView = false;
		for (ViewConfig view : views){
			if (view.getName().equals(name)){
				hasView = true;
				break;
			}
		}
		if (!hasView)
			return ;

		ViewConfig[] newViews = new ViewConfig[views.length-1];
		int i = 0;
		for (ViewConfig view : views){
			if (!view.getName().equals(name)){
				newViews[i] = view;
				i++;
			}
		}
		views = newViews;
	}

}
