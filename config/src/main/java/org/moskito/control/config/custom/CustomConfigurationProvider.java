package org.moskito.control.config.custom;

import org.moskito.control.config.ComponentConfig;
import org.moskito.control.config.datarepository.DataProcessingConfig;

import java.util.List;

/**
 * This interface allows third party software to manipulate configuration without actually changing configuration files.
 * Instead a configuration provider must be registered in the ComponentRegistry (i.e. by a Plugin) and will be used as additional configuration source.
 * Currently not all fields supported! Experemental!
 * @author lrosenberg
 * @since 10.08.20 22:39
 */

public interface CustomConfigurationProvider {
	/**
	 * Returns custom data processing config.
	 * @return
	 */
	DataProcessingConfig getDataProcessingConfig();


	/**
	 * Returns component configuration by this custom configuration provider. Unlike MoskitoControlConfiguration this method
	 * returns a list not an array, to make usage less cumbersome.
	 * @return
	 */
	List<ComponentConfig> getComponents();
}
