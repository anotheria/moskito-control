package org.moskito.control.data.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 06.06.18 14:44
 */
public abstract class AbstractDataProcessor implements DataProcessor{

	/**
	 * Target variable name.
	 */
	private String variableName;

	/**
	 * Dynamic per-class logger.
	 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void configure(String variable, String parameter) {
		variableName = variable;
		configureParameter(parameter);
	}

	abstract void configureParameter(String parameter);

	public String getVariableName() {
		return variableName;
	}

	protected Logger getLogger(){
		return logger;
	}
}
