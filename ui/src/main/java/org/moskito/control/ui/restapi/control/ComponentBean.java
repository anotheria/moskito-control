package org.moskito.control.ui.restapi.control;

import io.swagger.v3.oas.annotations.media.Schema;
import net.anotheria.util.NumberUtils;
import org.moskito.control.common.HealthColor;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * A component of an application.
 *
 * @author lrosenberg
 * @since 05.06.13 22:45
 */
@XmlRootElement
@Schema(name = "ComponentBean", description = "A component as part of the ControlResource")
public class ComponentBean {
	/**
	 * Name of the component.
	 */
	@XmlElement
	private String name;

	/**
	 * Category of the component.
	 */
	@XmlElement
	private String category;

	/**
	 * Current status of the category.
	 */
	@XmlElement
	private HealthColor color;

	/**
	 * Messages associated with the component.
	 */
	@XmlElement
	@Schema(description = "Messages associated with the component, usually only if Health is not green. Usually the values of thresholds that are not green.")
	private List<String> messages;

	/**
	 *
	 */
	@XmlElement
	private long lastUpdateTimestamp;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public HealthColor getColor() {
		return color;
	}

	public void setColor(HealthColor color) {
		this.color = color;
	}

	public void addMessage(String message) {
		messages.add(message);
	}

	public long getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(long lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

	@XmlElement(name="ISO8601Timestamp")
	public String getISOTimestamp(){
		return NumberUtils.makeISO8601TimestampString(lastUpdateTimestamp);
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
}

