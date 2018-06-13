package org.moskito.control.config.datarepository;

import com.google.gson.annotations.SerializedName;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

import java.util.Arrays;
import java.util.HashMap;

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
	@SerializedName("@processing")
	private String[] processing = new String[0];

	/**
	 * Configures how the preprocessors should be applied.
	 */
	@Configure
	@SerializedName("@preprocessing")
	private String[] preprocessing = new String[0];

	@Configure
	@SerializedName("@widgets")
	private WidgetConfig[] widgets = new WidgetConfig[0];


	@Configure
	@SerializedName("@retrievers")
	private RetrieverInstanceConfig[] retrievers = new RetrieverInstanceConfig[0];

	private HashMap<String, WidgetConfig> widgetConfigMap = new HashMap<>();

	public WidgetConfig[] getWidgets() {
		return widgets;
	}

	public void setWidgets(WidgetConfig[] widgets) {
		this.widgets = widgets;
		if (widgets!=null){
			widgetConfigMap = new HashMap<>();
			for (WidgetConfig wc : widgets){
				widgetConfigMap.put(wc.getName(), wc);
			}
		}
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

	public RetrieverInstanceConfig[] getRetrievers() {
		return retrievers;
	}

	public void setRetrievers(RetrieverInstanceConfig[] retrievers) {
		this.retrievers = retrievers;
	}

	@Override
	public String toString() {
		return "DataProcessingConfig{" +
				"processing=" + Arrays.toString(processing) +
				", preprocessing=" + Arrays.toString(preprocessing) +
				", widgets=" + Arrays.toString(widgets) +
				", retrievers=" + Arrays.toString(retrievers) +
				'}';
	}

	public WidgetConfig getWidget(String widgetName) {
		return widgetConfigMap.get(widgetName);
	}
}
