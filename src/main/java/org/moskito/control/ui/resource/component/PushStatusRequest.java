package org.moskito.control.ui.resource.component;

import org.moskito.control.core.HealthColor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author
 */
public class PushStatusRequest implements Serializable {

    private String name;
    private HealthColor status;
    private List<String> messages;
    private List<String> tags;
    private Map<String, String> attributes;
    private String category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public HealthColor getStatus() {
		return status;
	}

	public void setStatus(HealthColor status) {
		this.status = status;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return "PushStatusRequest{" +
				"name='" + name + '\'' +
				", status=" + status +
				", messages=" + messages +
				", tags=" + tags +
				", attributes=" + attributes +
				", category='" + category + '\'' +
				'}';
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
