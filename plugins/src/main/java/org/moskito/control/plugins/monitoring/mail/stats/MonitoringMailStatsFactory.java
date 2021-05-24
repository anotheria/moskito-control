package org.moskito.control.plugins.monitoring.mail.stats;

import net.anotheria.moskito.core.dynamic.IOnDemandStatsFactory;

/**
 * @author ynikonchuk
 */
public class MonitoringMailStatsFactory implements IOnDemandStatsFactory<MonitoringMailStats> {
	@Override
	public MonitoringMailStats createStatsObject(String name) {
		return new MonitoringMailStats(name);
	}
}
