package org.moskito.control.ui.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a single data widget at runtime.
 *
 * @author lrosenberg
 * @since 07.06.18 13:17
 */
public class DataWidgetBean {
	/**
	 * Widget type to select proper renderer.
	 */
	private String type;
	/**
	 * Widget caption.
	 */
	private String caption;
	/**
	 * Variables with values. This is widget specific.
	 */
	private Map<String,String> data = new HashMap<>();

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

	public void addData(String key, String value) {
		data.put(key,value);
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "DataWidgetBean{" +
				"type='" + type + '\'' +
				", caption='" + caption + '\'' +
				", data=" + data +
				'}';
	}
}
