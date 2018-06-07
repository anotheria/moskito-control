package org.moskito.control.config.datarepository;

import net.anotheria.util.StringUtils;
import org.configureme.annotations.ConfigureMe;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 07.06.18 13:13
 */
@ConfigureMe(allfields = true)
public class WidgetConfig {
	private String type;
	private String caption;
	private String mapping;

	private Map<String,String> mappings;

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
				'}';
	}
}
