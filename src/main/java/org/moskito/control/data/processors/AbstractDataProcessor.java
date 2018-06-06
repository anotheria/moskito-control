package org.moskito.control.data.processors;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 06.06.18 14:44
 */
public abstract class AbstractDataProcessor implements DataProcessor{

	private String variableName;

	@Override
	public void configure(String variable, String parameter) {
		variableName = variable;
		configureParameter(parameter);
	}

	abstract void configureParameter(String parameter);

	public String getVariableName() {
		return variableName;
	}
}
