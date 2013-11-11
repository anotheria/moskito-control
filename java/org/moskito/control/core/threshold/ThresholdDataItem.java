package org.moskito.control.core.threshold;

import org.moskito.control.core.HealthColor;

/**
 * Contains single threshold data.
 *
 * @author Vladyslav Bezuhlyi
 */
public class ThresholdDataItem {

    private String name;

    private HealthColor status;

    private String lastValue;

    private long statusChangeTimestamp;


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setStatus(HealthColor status) {
        this.status = status;
    }

    public HealthColor getStatus() {
        return status;
    }

    public void setLastValue(String lastValue) {
        this.lastValue = lastValue;
    }

    public String getLastValue() {
        return lastValue;
    }

    public void setStatusChangeTimestamp(long statusChangeTimestamp) {
        this.statusChangeTimestamp = statusChangeTimestamp;
    }

    public long getStatusChangeTimestamp() {
        return statusChangeTimestamp;
    }

}
