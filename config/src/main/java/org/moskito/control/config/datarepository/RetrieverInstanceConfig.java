package org.moskito.control.config.datarepository;

import com.google.gson.annotations.SerializedName;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.configureme.annotations.ConfigureMe;

import java.util.Arrays;

/**
 * This
 *
 * @author lrosenberg
 * @since 09.06.18 23:40
 */
@ConfigureMe
@SuppressFBWarnings(value={"EI_EXPOSE_REP", "EI_EXPOSE_REP2"}, justification="Configureme configs are designed in the way, that they expose the arrays.")
public class RetrieverInstanceConfig {
	/**
	 * Name of the retriever.
	 */
	@SerializedName("name")
	private String name;
	/**
	 * Configuration parameter.
	 */
	@SerializedName("configuration")
	private String configuration;
	/**
	 * Mappings for variables.
	 */
	@SerializedName("@mappings")
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
