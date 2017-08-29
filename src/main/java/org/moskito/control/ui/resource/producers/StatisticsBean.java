package org.moskito.control.ui.resource.producers;

import java.util.Set;

/**
 * @author strel
 */
public class StatisticsBean implements Comparable<StatisticsBean> {

    private String name;
    private Set<String> values;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getValues() {
        return values;
    }

    public void setValues(Set<String> values) {
        this.values = values;
    }

    @Override
    public int compareTo(StatisticsBean statisticsBean) {
        return name.compareTo(statisticsBean.getName());
    }
}
