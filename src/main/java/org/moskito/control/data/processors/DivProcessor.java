package org.moskito.control.data.processors;

import net.anotheria.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 04.06.18 23:57
 */
public class DivProcessor extends AbstractDataProcessor implements DataProcessor {

	private List<String> attributeNames = new LinkedList<>();

	@Override
	void configureParameter(String parameter) {
		String[] tokens = StringUtils.tokenize(parameter, ',');
		if (tokens.length != 2)
			throw new IllegalArgumentException("Expected exactly 2 parameters, got " + parameter);
		for (String t : tokens) {
			attributeNames.add(t.trim());
		}
	}

	@Override
	public Map<String, String> process(Map<String, String> data) {
		HashMap<String, String> ret = new HashMap<>();

		double value1 = Double.parseDouble(data.get(attributeNames.get(0)));
		double value2 = Double.parseDouble(data.get(attributeNames.get(1)));
		double result = value1 / value2;
		ret.put(getVariableName(), Double.valueOf(result).toString());
		return ret;
	}
}