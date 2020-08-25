package org.moskito.control.plugins;

/**
 * This interface defines a plugin-able object that can be loaded in moskito control for customization.
 *
 * @author lrosenberg
 */
public interface MoskitoControlPlugin {
	/**
	 * Called by the plugin repository.
	 * @param configurationName
	 */
	void setConfigurationName(String configurationName);

	/**
	 * Called after the plugin has been loaded and allows the initialization of the plugin.
	 */
	void initialize();

	/**
	 * Called before the plugin should be unloaded. The plugin should cleanup it traces in this method.
	 */
	void deInitialize();
}
