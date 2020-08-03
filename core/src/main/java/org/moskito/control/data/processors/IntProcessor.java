package org.moskito.control.data.processors;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to keep only integer part of a double. int 23.5 equals 23.
 *
 * @author lrosenberg
 * @since 07.06.18 13:02
 */
public class IntProcessor extends AbstractDataProcessor implements DataProcessor {

	/**
	 * Variable for the source value.
	 */
	private String sourceVariableName;

	@Override
	void configureParameter(String parameter) {
		sourceVariableName = parameter;
	}

	@Override
	public Map<String, String> process(Map<String, String> data) {
		HashMap<String, String> ret = new HashMap<>();

		double value = Double.parseDouble(data.get(sourceVariableName));
		String result = ""+(int)value;
		ret.put(getVariableName(), result);
		return ret;
	}
}