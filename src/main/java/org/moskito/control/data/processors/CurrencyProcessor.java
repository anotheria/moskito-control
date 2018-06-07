package org.moskito.control.data.processors;

import net.anotheria.util.NumberUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 07.06.18 12:46
 */
public class CurrencyProcessor extends AbstractDataProcessor implements DataProcessor {

	private String sourceVariableName;

	@Override
	void configureParameter(String parameter) {
		sourceVariableName = parameter;
	}

	@Override
	public Map<String, String> process(Map<String, String> data) {
		HashMap<String, String> ret = new HashMap<>();

		double value = Double.parseDouble(data.get(sourceVariableName));
		String result = NumberUtils.getCurrencyValue(value);
		ret.put(getVariableName(), result);
		return ret;
	}
}