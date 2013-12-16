package org.moskito.control.ui.bean;

/**
 * Contains single threshold of component.
 *
 * @author Vladyslav Bezuhlyi
 */
public class ThresholdBean {

    /**
     * THreshold name.
     */
    private String name;

    /**
     * String representation of threshold status.
     */
    private String status;

    /**
     * Last threshold value.
     */
    private String lastValue;

    /**
     * String representation of last threshold update timestamp.
     */
    private String statusChangeTimestamp;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastValue() {
        return lastValue;
    }

    public void setLastValue(String lastValue) {
        this.lastValue = lastValue;
    }

    public String getStatusChangeTimestamp() {
        return statusChangeTimestamp;
    }

    public void setStatusChangeTimestamp(String statusChangeTimestamp) {
        this.statusChangeTimestamp = statusChangeTimestamp;
    }

    @Override
    public String toString() {
        return name+" "+status+" "+lastValue;
    }

}
