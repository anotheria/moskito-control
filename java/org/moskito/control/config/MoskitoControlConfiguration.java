package org.moskito.control.config;

import org.configureme.ConfigurationManager;
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
public class MoskitoControlConfiguration {

	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(MoskitoControlConfiguration.class);

	/**
	 * Configured applications and their components.
	 */
	@Configure
	private ApplicationConfig[] applications;

	/**
	 * Configured connectors.
	 */
	@Configure
	private ConnectorConfig[] connectors;

	/**
	 * Number of elements to keep in the history per application.
	 */
	@Configure
	private int historyItemsAmount = 100;

    /**
     * Status change notifications muting time/period in minutes.
     */
    @Configure
    private int notificationsMutingTime;

	/**
	 * Configuration of the status updater. A default configuration is provided, so you don't need to overwrite it,
	 * except for tuning.
	 */
	@Configure
	private UpdaterConfig statusUpdater = new UpdaterConfig(10, 60, 10);

	/**
	 * Configuration of the charts updater. A default configuration is provided, so you don't need to overwrite it,
	 * except for tuning.
	 */
	@Configure
	private UpdaterConfig chartsUpdater = new UpdaterConfig(5, 60, 40);

	/**
	 * The application which is shown if no other application is selected.
	 */
	@Configure
	private String defaultApplication;

	/**
	 * If true - mail notification is enabled. Defaults to false.
	 */
	@Configure private boolean mailNotificationEnabled = false;

	/**
	 * If true, the usage is tracked via pixel.
	 */
	@Configure
	private boolean trackUsage = true;


	/**
	 * Returns the active configuration instance. The configuration object will update itself if the config is changed on disk.
	 * @return
	 */
	public static final MoskitoControlConfiguration getConfiguration(){
		return MoskitoControlConfigurationHolder.instance;
	}


	/**
	 * Loads a new configuration object from disk. This method is for unit testing.
	 * @return
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

	public boolean isMailNotificationEnabled() {
		return mailNotificationEnabled;
	}

	public void setMailNotificationEnabled(boolean mailNotificationEnabled) {
		this.mailNotificationEnabled = mailNotificationEnabled;
	}

	public boolean isTrackUsage() {
		return trackUsage;
	}

	public void setTrackUsage(boolean trackUsage) {
		this.trackUsage = trackUsage;
	}
}
