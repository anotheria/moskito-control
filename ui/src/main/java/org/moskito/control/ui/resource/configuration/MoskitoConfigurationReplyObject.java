package org.moskito.control.ui.resource.configuration;

import org.moskito.control.ui.resource.ControlReplyObject;
import org.moskito.control.ui.resource.NotificationsConfiguratorResource;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The reply object for the {@link NotificationsConfiguratorResource}.
 * @author strel
 */
@XmlRootElement
public class MoskitoConfigurationReplyObject extends ControlReplyObject {

	/**
	 * Configured applications and their components.
	 */
	@XmlElement
	private ApplicationConfigBean[] applications;

	/**
	 * Configured connectors.
	 */
	@XmlElement
	private ConnectorConfigBean[] connectors;

	/**
	 * Number of elements to keep in the history per application.
	 */
	@XmlElement
	private int historyItemsAmount;

	/**
	 * Status change notifications muting time/period in minutes.
	 */
	@XmlElement
	private int notificationsMutingTime;

	/**
	 * Configuration of the status updater. A default configuration is provided, so you don't need to overwrite it,
	 * except for tuning.
	 */
	@XmlElement
	private UpdaterConfigBean statusUpdater;

	/**
	 * Configuration of the charts updater. A default configuration is provided, so you don't need to overwrite it,
	 * except for tuning.
	 */
	@XmlElement
	private UpdaterConfigBean chartsUpdater;

	/**
	 * The application which is shown if no other application is selected.
	 */
	@XmlElement
	private String defaultApplication;

	/**
	 * If true, the usage is tracked via pixel.
	 */
	@XmlElement
	private boolean trackUsage;


	public ApplicationConfigBean[] getApplications() {
		return applications;
	}

	public void setApplications(ApplicationConfigBean[] applications) {
		this.applications = applications;
	}

	public ConnectorConfigBean[] getConnectors() {
		return connectors;
	}

	public void setConnectors(ConnectorConfigBean[] connectors) {
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

	public UpdaterConfigBean getStatusUpdater() {
		return statusUpdater;
	}

	public void setStatusUpdater(UpdaterConfigBean statusUpdater) {
		this.statusUpdater = statusUpdater;
	}

	public UpdaterConfigBean getChartsUpdater() {
		return chartsUpdater;
	}

	public void setChartsUpdater(UpdaterConfigBean chartsUpdater) {
		this.chartsUpdater = chartsUpdater;
	}

	public String getDefaultApplication() {
		return defaultApplication;
	}

	public void setDefaultApplication(String defaultApplication) {
		this.defaultApplication = defaultApplication;
	}

	public boolean isTrackUsage() {
		return trackUsage;
	}

	public void setTrackUsage(boolean trackUsage) {
		this.trackUsage = trackUsage;
	}
}
