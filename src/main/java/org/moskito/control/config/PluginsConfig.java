package org.moskito.control.config;

import com.google.gson.annotations.SerializedName;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

import java.io.Serializable;

/**
 * Config for plugins.
 *
 * @author lrosenberg
 * @since 19.03.13 15:48
 */
@ConfigureMe
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2", "EI_EXPOSE_REP"}, justification = "This is the way configureme works, it provides beans for access")
public class PluginsConfig implements Serializable {
	/**
	 * SerialVersionUID.
	 */
	private static final long serialVersionUID = -5882484953881475355L;

	/**
	 * Configured plugin list.
	 */
	@SerializedName("@plugins")
	@Configure private PluginConfig[] plugins;

	public PluginConfig[] getPlugins() {
		return plugins;
	}

	public void setPlugins(PluginConfig[] plugins) {
		this.plugins = plugins;
	}
}
