package org.moskito.control.config.datarepository;

import com.google.gson.annotations.SerializedName;

/**
 * Used to configure mappings.
 *
 * @author lrosenberg
 * @since 09.06.18 22:50
 */
public class VariableMapping {

	@SerializedName("variableName")
	private String variableName;

	@SerializedName("expression")
	private String expression;

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	@Override
	public String toString() {
		return "VariableMapping{" +
				"variableName='" + variableName + '\'' +
				", expression='" + expression + '\'' +
				'}';
	}
}
