package org.moskito.control.plugins.pagespeed;

import org.configureme.annotations.ConfigureMe;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 05.08.20 17:09
 */
@ConfigureMe(allfields = true)
public class PagespeedPluginTargetConfig {
	private String url;
	private String strategy;
	private String name;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "PagespeedPluginTargetConfig{" +
				"url='" + url + '\'' +
				", strategy='" + strategy + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
