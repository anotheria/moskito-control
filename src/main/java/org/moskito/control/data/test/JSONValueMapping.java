package org.moskito.control.data.test;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 09.06.18 00:11
 */
public class JSONValueMapping {
	private String jsonPathExpression;
	private String variableName;

	public JSONValueMapping(String jsonPathExpression, String variableName) {
		this.jsonPathExpression = jsonPathExpression;
		this.variableName = variableName;
	}

	public JSONValueMapping() {
	}

	public String getJsonPathExpression() {
		return jsonPathExpression;
	}

	public void setJsonPathExpression(String jsonPathExpression) {
		this.jsonPathExpression = jsonPathExpression;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
}
