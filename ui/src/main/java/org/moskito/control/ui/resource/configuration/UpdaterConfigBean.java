package org.moskito.control.ui.resource.configuration;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Configuration for status updater.
 */
@XmlRootElement
public class UpdaterConfigBean {
	/**
	 * Thread pool size.
	 */
	@XmlElement
	private int threadPoolSize = 10;
	/**
	 * Timeout in seconds after which connection attempt should be aborted.
	 */
	@XmlElement
	private int timeoutInSeconds = 60;

	/**
	 * How often a check should be started.
	 */
	@XmlElement
	private int checkPeriodInSeconds = 60;

	/**
	 * Used mainly for debugging to enable or disable an updater.
	 */
	@XmlElement
	private boolean enabled = true;


	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	public int getTimeoutInSeconds() {
		return timeoutInSeconds;
	}

	public void setTimeoutInSeconds(int timeoutInSeconds) {
		this.timeoutInSeconds = timeoutInSeconds;
	}

	public int getCheckPeriodInSeconds() {
		return checkPeriodInSeconds;
	}

	public void setCheckPeriodInSeconds(int checkPeriodInSeconds) {
		this.checkPeriodInSeconds = checkPeriodInSeconds;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
