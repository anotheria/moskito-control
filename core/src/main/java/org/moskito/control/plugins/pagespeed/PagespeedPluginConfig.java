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
	/**
	 * APIKey for google api.
	 */
	private String apiKey;
	@SerializedName("@targets")
	private PagespeedPluginTargetConfig[] targets;

	/**
	 * Provides a list of metric from page-speed that should be automatically charted.
	 */
	@SerializedName("@autoAccumulators")
	private String[] autoAccumulators;


	@SerializedName("@autoWidgets")
	private String[] autoWidgets;

	@SerializedName("@autoThresholds")
	private String[] autoThresholds;

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

	public String[] getAutoAccumulators() {
		return autoAccumulators;
	}

	public void setAutoAccumulators(String[] autoAccumulators) {
		this.autoAccumulators = autoAccumulators;
	}

	public String[] getAutoWidgets() {
		return autoWidgets;
	}

	public void setAutoWidgets(String[] autoWidgets) {
		this.autoWidgets = autoWidgets;
	}

	public static final PagespeedPluginConfig getByName(String name){
		PagespeedPluginConfig ret = new PagespeedPluginConfig();
		ConfigurationManager.INSTANCE.configureAs(ret, name);
		return ret;
	}

	public String[] getAutoThresholds() {
		return autoThresholds;
	}

	public void setAutoThresholds(String[] autoThresholds) {
		this.autoThresholds = autoThresholds;
	}
}
