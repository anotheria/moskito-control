package org.moskito.control.config;

import com.google.gson.annotations.SerializedName;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.connectors.ConnectorType;

/**
 * Configuration of a component.
 *
 * @author lrosenberg
 * @since 26.02.13 01:33
 */
@ConfigureMe
public class ComponentConfig {

	/**
	 * Name of the component.
	 */
	@Configure
	@SerializedName("name")
	private String name;

	/**
	 * Category.
	 */
	@Configure
	@SerializedName("category")
	private String category;

	/**
	 * Type of the connector for this component.
	 */
	@Configure
	@SerializedName("connectorType")
	private ConnectorType connectorType;

	/**
	 * Connector specific location.
	 */
	@Configure
	@SerializedName("location")
	private String location;

	/**
	 * Component tags.
	 */
	@Configure
	@SerializedName("tags")
	private String tags;

	/**
	 * Connector specific credentials.
	 */
	@Configure
	private String credentials;

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

	public ConnectorType getConnectorType() {
		return connectorType;
	}

	public void setConnectorType(ConnectorType connectorType) {
		this.connectorType = connectorType;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
}
