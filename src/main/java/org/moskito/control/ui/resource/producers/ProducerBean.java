package org.moskito.control.ui.resource.producers;

import java.util.Set;

/**
 * @author strel
 */
public class ProducerBean implements Comparable<ProducerBean> {

    /**
     * Producer name.
     */
    private String name;

    /**
     *
     */
    private Set<StatisticsBean> stats;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<StatisticsBean> getStats() {
        return stats;
    }

    public void setStats(Set<StatisticsBean> stats) {
        this.stats = stats;
    }

    @Override
    public int compareTo(ProducerBean producerBean) {
        return name.compareTo(producerBean.getName());
    }
}
