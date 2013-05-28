package org.anotheria.moskito.control.config;

import org.configureme.annotations.ConfigureMe;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 28.05.13 21:28
 */
@ConfigureMe(allfields = true)
public class UpdaterConfig {
	private int threadPoolSize = 10;

	private int timeout = 60;

	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
