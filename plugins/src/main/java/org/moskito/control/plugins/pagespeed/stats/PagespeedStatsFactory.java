package org.moskito.control.plugins.pagespeed.stats;

import net.anotheria.moskito.core.dynamic.IOnDemandStatsFactory;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 */
public class PagespeedStatsFactory implements IOnDemandStatsFactory<PagespeedStats> {
	@Override
	public PagespeedStats createStatsObject(String name) {
		return new PagespeedStats(name);
	}
}