package org.moskito.control.config;

import com.google.gson.annotations.SerializedName;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	 * Configured applications and their components.
	 */
	@Configure
	@SerializedName("@applications")
	private ApplicationConfig[] applications;

	/**
	 * Configured connectors.
	 */
	@Configure
	@SerializedName("@connectors")
	private ConnectorConfig[] connectors;

	/**
	 * Number of elements to keep in the history per application.
	 */
	@Configure
	@SerializedName("historyItemsAmount")
	private int historyItemsAmount = 100;

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
	 * The application which is shown if no other application is selected.
	 */
	@Configure
	@SerializedName("defaultApplication")
	private String defaultApplication;

	/**
	 * Config objects for plugins.
	 */
	@Configure
	@SerializedName("@pluginsConfig")
	private PluginsConfig pluginsConfig = new PluginsConfig();

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

	public ApplicationConfig getApplication(String name){
		for (ApplicationConfig a : applications){
			if (a.getName().equals(name))
				return a;
		}
		throw new IllegalArgumentException("App with name "+name+" not found");
	}

    public ApplicationConfig[] getApplications() {
		return applications;
	}

	public void setApplications(ApplicationConfig[] applications) {
		this.applications = applications;
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

    public String getDefaultApplication() {
		return defaultApplication == null ? "" : defaultApplication;
	}

	public void setDefaultApplication(String defaultApplication) {
		this.defaultApplication = defaultApplication;
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

    @AfterConfiguration
    public void checkConfigData() {
        for(ApplicationConfig config : applications)
            if (config.getName().equals(defaultApplication))
                return;
        log.warn("Wrong default application: {}. There is no such application in the list.", defaultApplication);
    }
}
