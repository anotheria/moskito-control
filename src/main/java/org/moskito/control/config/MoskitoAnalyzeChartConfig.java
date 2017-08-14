package org.moskito.control.config;

import com.google.gson.annotations.SerializedName;
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
    @SerializedName("name")
    private String name;

    /**
     * Interval name / type.
     */
    @Configure
    @SerializedName("interval")
    private String interval;

    /**
     * Chart type, i.e. what chart should actually show
     * ( total, average value and so on).
     */
    @Configure
    @SerializedName("type")
    private String type;

    @Configure
    @SerializedName("@hosts")
    private String[] hosts;

    @Configure
    @SerializedName("@producers")
    private ProducerConfig[] producers;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String[] getHosts() {
        return hosts;
    }

    public void setHosts(String[] hosts) {
        this.hosts = hosts;
    }

    public ProducerConfig[] getProducers() {
        return producers;
    }

    public void setProducers(ProducerConfig[] producers) {
        this.producers = producers;
    }
}
