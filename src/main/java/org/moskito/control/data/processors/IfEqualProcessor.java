package org.moskito.control.data.processors;

import net.anotheria.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores target value in the targetVariable if two variables are equal.
 *
 * @author lrosenberg
 * @since 10.06.18 00:01
 */
public class IfEqualProcessor extends AbstractDataProcessor implements DataProcessor {
	/**
	 * Variable to get first value from.
	 */
	private String conditionVariable;
	/**
	 * Variable to get second value from.
	 */
	private String valueVariable;
	/**
	 * Return value in case both variables match.
	 */
	private String targetValue;



	@Override
	void configureParameter(String parameter) {
		String[] tokens = StringUtils.tokenize(parameter, ',');
		conditionVariable = tokens[0];
		valueVariable = tokens[1];
		targetValue = tokens[2];

	}
	@Override
	public Map<String, String> process(Map<String, String> data) {
		HashMap<String, String> ret = new HashMap<>();

		String val1 = data.get(conditionVariable);
		String val2 = data.get(valueVariable);

		if (val1==null || val1.length()==0) {
			if (val2==null || val2.length()==0){
				ret.put(getVariableName(), targetValue);
				return ret;
			}else{
				//val2 is not zero, but val1 is
				return ret;
			}
		}

		if (val2==null || val2.length()==0){
			return ret;
		}

		if (val1.equals(val2))
			ret.put(getVariableName(), targetValue);
		return ret;
	}



}