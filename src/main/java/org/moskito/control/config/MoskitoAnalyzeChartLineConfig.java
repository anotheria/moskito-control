package org.moskito.control.config;

import com.google.gson.annotations.SerializedName;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

@ConfigureMe
public class MoskitoAnalyzeChartLineConfig {

    @Configure
    @SerializedName("name")
    private String name;

    @Configure
    @SerializedName("producer")
    private String producer;

    @Configure
    @SerializedName("stat")
    private String stat;

    @Configure
    @SerializedName("value")
    private String value;

    @Configure
    @SerializedName("@components")
    private String[] components;

    @Configure
    @SerializedName("average")
    private boolean average;

    @Configure
    @SerializedName("baseline")
    private boolean baseline;


    public MoskitoAnalyzeChartLineConfig() {

    }

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

    public String[] getComponents() {
        return components;
    }

    public void setComponents(String[] components) {
        this.components = components;
    }

    public boolean isAverage() {
        return average;
    }

    public void setAverage(boolean average) {
        this.average = average;
    }

    public boolean isBaseline() {
        return baseline;
    }

    public void setBaseline(boolean baseline) {
        this.baseline = baseline;
    }
}
