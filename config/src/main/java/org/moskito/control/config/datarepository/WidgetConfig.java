package org.moskito.control.config.datarepository;

import com.google.gson.annotations.SerializedName;
import net.anotheria.util.StringUtils;
import org.configureme.annotations.ConfigureMe;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration for data repository widget.
 *
 * @author lrosenberg
 * @since 07.06.18 13:13
 */
@ConfigureMe(allfields = true)
public class WidgetConfig {
	/**
	 * Type of widget. Supported widget type should be listed in moskito-control-confluence-space
	 * https://confluence.opensource.anotheria.net/display/MSK/DataRepository.
	 */
	@SerializedName("type")
	private String type;
	/**
	 * Caption text.
	 */
	@SerializedName("caption")
	private String caption;
	/**
	 * String with mappings of input values to data variables.
	 */
	@SerializedName("mapping")
	private String mapping;

	/**
	 * Parsed mapping for faster access.
	 */
	private Map<String,String> mappings;
	/**
	 * Name of the widget for reference in config.
	 */
	@SerializedName("name")
	private String name;

	@SerializedName("tags")
	private String tags;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getMapping() {
		return mapping;
	}

	public void setMapping(String mapping) {
		this.mapping = mapping;
		mappings = new HashMap<>();
		String pairs[] = StringUtils.tokenize(mapping, ',');
		for (String pair : pairs){
			String[] keyvalue = StringUtils.tokenize(pair, '=');
			String key = keyvalue[0].trim();
			String value = keyvalue[1].trim();
			mappings.put(key, value);

		}
	}

	public Map<String,String> getMappings(){
		return mappings;
	}

	@Override
	public String toString() {
		return "WidgetConfig{" +
				"type='" + type + '\'' +
				", caption='" + caption + '\'' +
				", mapping='" + mapping + '\'' +
				", mappings=" + mappings +
				", name='" + name + '\'' +
				'}';
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
}
