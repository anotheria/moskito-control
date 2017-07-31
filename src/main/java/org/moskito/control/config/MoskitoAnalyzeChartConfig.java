package org.moskito.control.config;

import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

/**
 * Represents chart properties used in request to MoSKito-Analyze.
 * @author strel
 */
@ConfigureMe
public class MoskitoAnalyzeChartConfig {

    /**
     * Chart name used as caption.
     */
    @Configure
    private String name;

    /**
     * Producer name.
     */
    @Configure
    private String producer;

    /**
     * Stat name.
     */
    @Configure
    private String stat;

    /**
     * Value name.
     */
    @Configure
    private String value;

    /**
     * Interval name / type.
     */
    @Configure
    private String interval;

    /**
     * Chart type, i.e. what chart should actually show
     * ( total, average value and so on).
     */
    @Configure
    private String type;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
