package org.moskito.control.data.processors;

import net.anotheria.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Checks multiple numeric input variables and retrieves the min value. The value is stored into target variable.
 */
public class MinProcessor extends AbstractDataProcessor implements DataProcessor {
	/**
	 * Variables to check.
	 */
    private List<String> attributeNames = new LinkedList<>();

    @Override
    void configureParameter(String parameter) {
        String[] tokens = StringUtils.tokenize(parameter, ',');
        for (String t : tokens) {
            attributeNames.add(t.trim());
        }
    }

    @Override
    public Map<String, String> process(Map<String, String> data) {
        HashMap<String, String> ret = new HashMap<>();
        double min = Double.POSITIVE_INFINITY;
        boolean checked = false;

        for (String name : attributeNames) {
            String val = data.get(name);

            if (val == null || val.length() == 0) {
                continue;
            }

            try {
                double valAsDouble = Double.parseDouble(val);
                min = (valAsDouble < min) ? valAsDouble : min;
                checked = true;
            } catch (NumberFormatException e) {
				getLogger().warn("Can't parse variable "+name+" value:"+val+", skipped");
            }
        }

        if (checked) {
            ret.put(getVariableName(), Double.valueOf(min).toString());
        }

        return ret;
    }
}
