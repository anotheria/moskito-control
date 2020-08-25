package org.moskito.controlagent.info;

import org.moskito.controlagent.data.info.UptimeProvider;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 24.07.17 22:36
 */
public class TestUptimeProvider implements UptimeProvider{
	private long value;

	public TestUptimeProvider(long aValue){
		value = aValue;
	}

	@Override
	public long getUptime() {
		return value;
	}
}
