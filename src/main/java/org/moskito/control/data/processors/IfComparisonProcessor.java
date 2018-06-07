package org.moskito.control.data.processors;

import net.anotheria.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 07.06.18 18:16
 */
public abstract class IfComparisonProcessor extends AbstractDataProcessor implements DataProcessor {

	private String conditionVariable;
	private String conditionValue;



	@Override
	void configureParameter(String parameter) {
		String[] tokens = StringUtils.tokenize(parameter, ',');
		conditionVariable = tokens[0];
		conditionValue = tokens[1];
	}
	@Override
	public Map<String, String> process(Map<String, String> data) {
		HashMap<String, String> ret = new HashMap<>();

		String val = data.get(conditionVariable);
		if (val==null || val.length()==0)
			return ret;
		double value = Double.parseDouble(val);
		if (conditionFullfilled(value))
			ret.put(getVariableName(), conditionValue);
		return ret;
	}

	abstract boolean conditionFullfilled(double value);

}