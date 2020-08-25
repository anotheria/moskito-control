package org.moskito.control.data.processors;

import net.anotheria.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 07.06.18 17:50
 */
public class CalculatePercentageDeviationProcessor extends AbstractDataProcessor implements DataProcessor{
		private String currentValueVariable;
		private String baseValueVariable;

		@Override
		void configureParameter(String parameter) {
			String[] tokens = StringUtils.tokenize(parameter, ',');
			currentValueVariable = tokens[0];
			baseValueVariable = tokens[1];
		}

		@Override
		public Map<String, String> process(Map<String, String> data) {
			HashMap<String,String> ret = new HashMap<>();

			String cvv = data.get(currentValueVariable);
			String bvv = data.get(baseValueVariable);
			if (cvv==null || cvv.length()==0 || bvv==null || bvv.length()==0)
				return ret;
			double baseValue = Double.parseDouble(bvv);
			double currentValue = Double.parseDouble(cvv);

			double result = (currentValue / baseValue)*100 - 100;
			//beautify result (cut everything after comma but 1 digit, so the result is something like 23.1
			result = ((double)((int)(result * 10 + 0.5)))/10;

			ret.put(getVariableName(), Double.valueOf(result).toString());
			return ret;
		}
}
