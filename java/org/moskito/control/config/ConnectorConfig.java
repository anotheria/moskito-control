package org.moskito.control.config;

import org.moskito.control.connectors.ConnectorType;
import org.configureme.annotations.ConfigureMe;

/**
 * Represents a single configured connector.
 *
 * @author lrosenberg
 * @since 28.05.13 20:59
 */
@ConfigureMe (allfields = true)
public class ConnectorConfig {
	private ConnectorType type;

	private String className;

	public ConnectorType getType() {
		return type;
	}

	public void setType(ConnectorType type) {
		this.type = type;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override public String toString(){
		return getType()+": "+getClassName();
	}
}
