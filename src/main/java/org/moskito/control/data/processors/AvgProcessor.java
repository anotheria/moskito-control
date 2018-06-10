package org.moskito.control.data.processors;

import net.anotheria.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AvgProcessor extends AbstractDataProcessor implements DataProcessor {
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
        double sum = 0;
        int amount = 0;

        for (String name : attributeNames) {
            String val = data.get(name);

            if (val == null || val.length() == 0) {
                continue;
            }

            try {
                double valAsDouble = Double.parseDouble(val);
                sum += valAsDouble;
                amount++;
            } catch (NumberFormatException e) {
                getLogger().warn("Can't parse variable "+name+" value:"+val+", skipped");
            }
        }

        if (amount > 0) {
            ret.put(getVariableName(), Double.valueOf(sum / amount).toString());
        }

        return ret;
    }
}
