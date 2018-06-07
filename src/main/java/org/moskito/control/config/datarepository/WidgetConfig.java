package org.moskito.control.config.datarepository;

import org.configureme.annotations.ConfigureMe;

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
	private String data;

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

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "WidgetConfig{" +
				"type='" + type + '\'' +
				", caption='" + caption + '\'' +
				", data='" + data + '\'' +
				'}';
	}
}
