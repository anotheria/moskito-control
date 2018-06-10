package org.moskito.control.data.processors;

import java.util.HashMap;
import java.util.Map;

/**
 * This processor stores a constant value as a variable. Useful if you want to use this value in calculation later.
 *
 * @author lrosenberg
 * @since 04.06.18 23:58
 */
public class ConstantProcessor extends AbstractDataProcessor implements DataProcessor{

	private HashMap<String,String> constant = null;

	public ConstantProcessor(){
	}

	@Override
	void configureParameter(String parameter) {
		constant = new HashMap();
		constant.put(getVariableName(), parameter);

	}

	@Override
	public Map<String, String> process(Map<String, String> data) {
		return constant;
	}
}
