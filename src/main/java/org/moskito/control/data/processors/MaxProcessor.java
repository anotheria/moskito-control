package org.moskito.control.data.processors;

import net.anotheria.util.StringUtils;

import java.util.*;

public class MaxProcessor  extends AbstractDataProcessor implements DataProcessor {

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
        double max = Double.NEGATIVE_INFINITY;
        boolean checked = false;

        for (String name : attributeNames) {
            String val = data.get(name);

            if (val == null || val.length() == 0) {
                continue;
            }

            try {
                double valAsDouble = Double.parseDouble(val);
                max = (valAsDouble > max) ? valAsDouble : max;
                checked = true;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if (checked) {
            ret.put(getVariableName(), Double.valueOf(max).toString());
        }

        return ret;
    }
}
