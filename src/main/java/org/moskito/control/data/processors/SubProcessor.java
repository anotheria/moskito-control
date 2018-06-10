package org.moskito.control.data.processors;

import net.anotheria.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Substracts multiple values from the first parameter.
 */
public class SubProcessor extends AbstractDataProcessor implements DataProcessor {
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
        List<Double> matches = new LinkedList<>();

        for (String name : attributeNames) {
            String val = data.get(name);

            if (val == null || val.length() == 0) {
                continue;
            }

            try {
                matches.add(new Double(val));
            } catch (NumberFormatException e) {
				getLogger().warn("Can't parse variable "+name+" value:"+val+", skipped");
            }
        }

        if (matches.size() > 0) {
            double sub = matches.get(0);

            for (int i = 1; i < matches.size(); i++) {
                sub -= matches.get(i);
            }

            ret.put(getVariableName(), Double.valueOf(sub).toString());
        }

        return ret;
    }
}
