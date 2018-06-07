package org.moskito.control.config.datarepository;

import com.google.gson.annotations.SerializedName;
import org.configureme.annotations.Configure;

import java.util.Arrays;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 06.06.18 12:21
 */
public class DataRepositoryConfig {
	/**
	 * Processor definitions. Which kind of processors are known to the system.
	 */
	@Configure
	@SerializedName("@processors")
	private ProcessorConfig[] processors;

	/**
	 * Configures how the processors should be applied.
	 */
	@Configure
	private String[] processing;

	public ProcessorConfig[] getProcessors() {
		return processors;
	}

	public void setProcessors(ProcessorConfig[] processors) {
		this.processors = processors;
	}


	public String[] getProcessing() {
		return processing;
	}

	public void setProcessing(String[] processing) {
		this.processing = processing;
	}

	@Override
	public String toString() {
		return "DataRepositoryConfig{" +
				"processors=" + Arrays.toString(processors) +
				", processing=" + Arrays.toString(processing) +
				'}';
	}
}
