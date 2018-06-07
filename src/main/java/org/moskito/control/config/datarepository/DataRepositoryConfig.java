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
	 * Preprocessors definitions. Which kind of processors are known to the system. Preprocessors are different from processors, they are active before the calculation
	 * takes place.
	 */
	@Configure
	@SerializedName("@preprocessors")
	private ProcessorConfig[] preprocessors;

	/**
	 * Configures how the processors should be applied.
	 */
	@Configure
	private String[] processing;

	/**
	 * Configures how the preprocessors should be applied.
	 */
	@Configure
	private String[] preprocessing;

	@Configure
	private WidgetConfig[] widgets;

	public ProcessorConfig[] getProcessors() {
		return processors;
	}

	public void setProcessors(ProcessorConfig[] processors) {
		this.processors = processors;
	}


	public WidgetConfig[] getWidgets() {
		return widgets;
	}

	public void setWidgets(WidgetConfig[] widgets) {
		this.widgets = widgets;
	}

	public String[] getProcessing() {
		return processing;
	}

	public void setProcessing(String[] processing) {
		this.processing = processing;
	}

	public ProcessorConfig[] getPreprocessors() {
		return preprocessors;
	}

	public void setPreprocessors(ProcessorConfig[] preprocessors) {
		this.preprocessors = preprocessors;
	}

	public String[] getPreprocessing() {
		return preprocessing;
	}

	public void setPreprocessing(String[] preprocessing) {
		this.preprocessing = preprocessing;
	}

	@Override
	public String toString() {
		return "DataRepositoryConfig{" +
				"processors=" + Arrays.toString(processors) +
				", preprocessors=" + Arrays.toString(preprocessors) +
				", processing=" + Arrays.toString(processing) +
				", preprocessing=" + Arrays.toString(preprocessing) +
				", widgets=" + Arrays.toString(widgets) +
				'}';
	}
}
