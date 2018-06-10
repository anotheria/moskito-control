package org.moskito.control.data.processors;

import net.anotheria.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a macro style if-greater-or-less-or-zero condition. Used to shorten expression, otherwise
 * we would need three expressions to achieve same result.
 *
 * @author lrosenberg
 * @since 09.06.18 15:55
 */
public class IfGLZProcessor extends AbstractDataProcessor implements DataProcessor {

	/**
	 * Variable to check for &gt;0, &lt;0 or =0.
	 */
	private String conditionVariable;
	/**
	 * Result in case condition value if greater zero.
	 */
	private String conditionValueGreater;
	/**
	 * Result in case condition value is less than zero.
	 */
	private String conditionValueLess;
	/**
	 * Result in case condition value is zero.
	 */
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