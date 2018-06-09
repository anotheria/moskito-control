package org.moskito.control.config.datarepository;

import org.configureme.annotations.ConfigureMe;

/**
 * This class defines a retriever.
 *
 * @author lrosenberg
 * @since 06.06.18 13:48
 */
@ConfigureMe (allfields = true)
public class RetrieverConfig {
	/**
	 * Name of the processor.
	 */
	private String name;
	/**
	 * Name of the implementation class for this processor.
	 */
	private String clazz;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String toString(){
		return name + "=" + clazz;
	}
}
