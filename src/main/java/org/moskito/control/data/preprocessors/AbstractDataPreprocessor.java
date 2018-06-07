package org.moskito.control.data.preprocessors;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 06.06.18 14:44
 */
public abstract class AbstractDataPreprocessor implements DataPreprocessor{

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
