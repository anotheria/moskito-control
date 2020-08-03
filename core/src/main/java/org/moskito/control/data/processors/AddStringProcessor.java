package org.moskito.control.data.processors;

import java.util.HashMap;
import java.util.Map;

/**
 * This processor adds a string to an existing variable and stores it under the same name.
 *
 * @author lrosenberg
 * @since 07.06.18 17:19
 */
public class AddStringProcessor extends AbstractDataProcessor implements DataProcessor {

	private String stringToAdd;

	@Override
	void configureParameter(String parameter) {
		stringToAdd = parameter;
	}

	@Override
	public Map<String, String> process(Map<String, String> data) {
		HashMap<String, String> ret = new HashMap<>();

		String source = data.get(getVariableName());
		if (source==null)
			return ret;
		String result = source + " "+stringToAdd;
		ret.put(getVariableName(), result);
		return ret;
	}
}