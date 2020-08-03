package org.moskito.controlagent.data.info;

import java.lang.management.ManagementFactory;

/**
 * JMX Based uptime provider.
 *
 * @author lrosenberg
 * @since 24.07.17 22:34
 */
public class DefaultUptimeProvider implements UptimeProvider{
	@Override
	public long getUptime() {
		return ManagementFactory.getRuntimeMXBean().getUptime();
	}
}
