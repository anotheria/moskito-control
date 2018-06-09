package org.moskito.control.config.datarepository;

import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

import java.util.Arrays;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 06.06.18 12:21
 */
@ConfigureMe
public class DataProcessingConfig {
	/**
	 * Configures how the processors should be applied.
	 */
	@Configure
	private String[] processing = new String[0];

	/**
	 * Configures how the preprocessors should be applied.
	 */
	@Configure
	private String[] preprocessing = new String[0];

	@Configure
	private WidgetConfig[] widgets = new WidgetConfig[0];

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

	public String[] getPreprocessing() {
		return preprocessing;
	}

	public void setPreprocessing(String[] preprocessing) {
		this.preprocessing = preprocessing;
	}

	@Override
	public String toString() {
		return "DataProcessingConfig{" +
				"processing=" + Arrays.toString(processing) +
				", preprocessing=" + Arrays.toString(preprocessing) +
				", widgets=" + Arrays.toString(widgets) +
				'}';
	}
}
