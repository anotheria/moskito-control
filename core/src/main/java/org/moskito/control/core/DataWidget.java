package org.moskito.control.core;

import net.anotheria.util.StringUtils;
import org.moskito.control.config.datarepository.WidgetConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents a Data Widget.
 *
 * @author lrosenberg
 * @since 2019-08-28 11:42
 */
public class DataWidget {

	/**
	 * Tags associated with this widget.
	 */
	private List<String> tags = Collections.emptyList();
	/**
	 * Config object for this widget.
	 */
	private WidgetConfig config;

	/**
	 * Used to provide generic names for instances without a name.
	 */
	private static AtomicLong instanceCounter = new AtomicLong();

	/**
	 * Widget name.
	 */
	private String name;

	public DataWidget(WidgetConfig widgetConfig){
		config = widgetConfig;
		if (config.getTags()!=null && config.getTags().length()>0)
			tags = Arrays.asList(StringUtils.tokenize(config.getTags(), ','));

		if (widgetConfig.getName()==null || widgetConfig.getName().length()==0)
			name = "Widget-"+instanceCounter.incrementAndGet();
		else
			name = widgetConfig.getName();
	}

	public String getName(){
		return name;
	}

	public List<String> getTags() {
		return tags;
	}

	public String getType(){
		return config.getType();
	}

	public String getCaption(){
		return config.getCaption();
	}

	public Map<String, String> getMappings() {
		return config.getMappings();
	}

	@Override
	public String toString() {
		return "DataWidget{" +
				"tags=" + tags +
				", config=" + config +
				'}';
	}
}
