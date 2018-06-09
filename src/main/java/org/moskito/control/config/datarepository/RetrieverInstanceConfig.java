package org.moskito.control.config.datarepository;

import org.configureme.annotations.ConfigureMe;

import java.util.Arrays;

/**
 * This
 *
 * @author lrosenberg
 * @since 09.06.18 23:40
 */
@ConfigureMe
public class RetrieverInstanceConfig {
	/**
	 * Name of the retriever.
	 */
	private String name;
	/**
	 * Configuration parameter.
	 */
	private String configuration;
	/**
	 * Mappings for variables.
	 */
	private VariableMapping[] mappings;

	public String getConfiguration() {
		return configuration;
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	public VariableMapping[] getMappings() {
		return mappings;
	}

	public void setMappings(VariableMapping[] mappings) {
		this.mappings = mappings;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "RetrieverInstanceConfig{" +
				"name='" + name + '\'' +
				", configuration='" + configuration + '\'' +
				", mappings=" + Arrays.toString(mappings) +
				'}';
	}
}
