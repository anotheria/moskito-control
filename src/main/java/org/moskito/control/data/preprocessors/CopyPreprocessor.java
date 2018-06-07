package org.moskito.control.data.preprocessors;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 07.06.18 14:30
 */
public class CopyPreprocessor extends AbstractDataPreprocessor{
	private String sourceVariableName;

	@Override
	void configureParameter(String parameter) {
		sourceVariableName = parameter;
	}

	@Override
	public Map<String, String> process(Map<String, String> oldData) {
		HashMap<String, String> ret = new HashMap<>();
		String oldValue = oldData.get(sourceVariableName);
		if (oldValue!=null)
			ret.put(getVariableName(), oldValue);
		return ret;
	}
}
