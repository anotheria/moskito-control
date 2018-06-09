package org.moskito.control.data.processors;

import net.anotheria.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 09.06.18 15:55
 */
public class IfGLZProcessor extends AbstractDataProcessor implements DataProcessor {

	private String conditionVariable;
	private String conditionValueGreater;
	private String conditionValueLess;
	private String conditionValueZero;



	@Override
	void configureParameter(String parameter) {
		String[] tokens = StringUtils.tokenize(parameter, ',');
		conditionVariable = tokens[0];
		conditionValueGreater = tokens[1];
		conditionValueLess = tokens[2];
		conditionValueZero = tokens[3];

	}
	@Override
	public Map<String, String> process(Map<String, String> data) {
		HashMap<String, String> ret = new HashMap<>();

		String val = data.get(conditionVariable);
		if (val==null || val.length()==0)
			return ret;
		double value = Double.parseDouble(val);

		String conditionValue = value == 0?
			conditionValueZero : value > 0 ?
			conditionValueGreater : conditionValueLess;

		ret.put(getVariableName(), conditionValue);
		return ret;
	}



}