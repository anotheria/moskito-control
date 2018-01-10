package org.moskito.control.ui.resource.analyze;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MoskitoAnalyzeChartLineBean {

    @XmlElement
    private String name;

    @XmlElement
    private String producer;

    @XmlElement
    private String stat;

    @XmlElement
    private String value;

    @XmlElement
    private String[] components;

    @XmlElement
    private boolean average;

    @XmlElement
    private boolean baseline;

    public MoskitoAnalyzeChartLineBean() {

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
