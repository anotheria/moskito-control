package org.moskito.control.config.datarepository;

import com.google.gson.annotations.SerializedName;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

import java.util.Arrays;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 06.06.18 12:21
 */
@ConfigureMe(name="datarepository", allfields = true)
@SuppressFBWarnings(value={"EI_EXPOSE_REP", "EI_EXPOSE_REP2"}, justification="Configureme configs are designed in the way, that they expose the arrays.")
public class DataRepositoryConfig {
	/**
	 * Processor definitions. Which kind of processors are known to the system.
	 */
	@Configure
	@SerializedName("@processors")
	private ProcessorConfig[] processors;

	/**
	 * Preprocessors definitions. Which kind of processors are known to the system. Preprocessors are different from processors, they are active before the calculation
	 * takes place.
	 */
	@Configure
	@SerializedName("@preprocessors")
	private ProcessorConfig[] preprocessors;

	@Configure
	@SerializedName("@retrievers")
	private RetrieverConfig[] retrievers;

	public ProcessorConfig[] getProcessors() {
		return processors;
	}

	public void setProcessors(ProcessorConfig[] processors) {
		this.processors = processors;
	}

	public ProcessorConfig[] getPreprocessors() {
		return preprocessors;
	}

	public void setPreprocessors(ProcessorConfig[] preprocessors) {
		this.preprocessors = preprocessors;
	}

	public RetrieverConfig[] getRetrievers() {
		return retrievers;
	}

	public void setRetrievers(RetrieverConfig[] retrievers) {
		this.retrievers = retrievers;
	}

	@Override
	public String toString() {
		return "DataRepositoryConfig{" +
				"processors=" + Arrays.toString(processors) +
				", preprocessors=" + Arrays.toString(preprocessors) +
				", retrievers=" + Arrays.toString(retrievers) +
				'}';
	}
}
