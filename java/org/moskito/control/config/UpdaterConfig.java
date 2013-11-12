package org.moskito.control.config;

import org.configureme.annotations.ConfigureMe;

/**
 * Configuration for status updater.
 *
 * @author lrosenberg
 * @since 28.05.13 21:28
 */
@ConfigureMe(allfields = true)
public class UpdaterConfig {
	/**
	 * Thread pool size.
	 */
	private int threadPoolSize = 10;
	/**
	 * Timeout in seconds after which connection attempt should be aborted.
	 */
	private int timeoutInSeconds = 60;

	/**
	 * How often a check should be started.
	 */
	private int checkPeriodInSeconds = 60;

	/**
	 * Used mainly for debugging to enable or disable an updater.
	 */
	private boolean enabled = true;

	public UpdaterConfig(){

	}

	public UpdaterConfig(int aThreadPoolSize, int aTimeoutInSeconds, int aCheckPeriodInSeconds){
		threadPoolSize = aThreadPoolSize;
		timeoutInSeconds = aTimeoutInSeconds;
		checkPeriodInSeconds = aCheckPeriodInSeconds;
	}

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

	@Override public String toString(){
		return enabled ?
				"checkPeriod: "+getCheckPeriodInSeconds()+", poolSize: "+getThreadPoolSize()+", Timeout: "+getTimeoutInSeconds()+" sec" :
				"disabled";
	}


}
