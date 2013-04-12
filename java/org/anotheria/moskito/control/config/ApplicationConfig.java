package org.anotheria.moskito.control.config;

import org.configureme.annotations.Configure;

/**
 * Configuration of the application.
 *
 * @author lrosenberg
 * @since 26.02.13 01:33
 */
public class ApplicationConfig {
	/**
	 * Name of the application.
	 */
	@Configure private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
