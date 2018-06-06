package org.moskito.control.config.datarepository;

import org.configureme.annotations.ConfigureMe;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 06.06.18 14:07
 */
@ConfigureMe (allfields = true)
public class ProcessingConfig {
	/**
	 * Name of the processor.
	 */
	private String processor;
	/**
	 * Target variable name.
	 */
	private String variable;
	/**
	 * Parameter for the processor.
	 */
	private String parameter;

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
}
