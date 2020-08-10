package org.moskito.control.plugins.pagespeed;

import com.google.gson.annotations.SerializedName;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.ConfigureMe;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 05.08.20 17:08
 */
@ConfigureMe(allfields = true)
public class PagespeedPluginConfig {
	private String apiKey;
	@SerializedName("@targets")
	private PagespeedPluginTargetConfig[] targets;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public PagespeedPluginTargetConfig[] getTargets() {
		return targets;
	}

	public void setTargets(PagespeedPluginTargetConfig[] targets) {
		this.targets = targets;
	}

	public static final PagespeedPluginConfig getByName(String name){
		PagespeedPluginConfig ret = new PagespeedPluginConfig();
		ConfigurationManager.INSTANCE.configureAs(ret, name);
		return ret;
	}
	

}
