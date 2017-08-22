package org.moskito.control.ui.resource.analyze;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents chart parameters which will be requested.
 * @author strel
 */
@XmlRootElement
public class MoskitoAnalyzeChartBean {

    @XmlElement
    private String id;

    /**
     * Chart name.
     */
    @XmlElement
    private String name;

    /**
     * Chart caption.
     */
    @XmlElement
    private String caption;

    /**
     * Interval name / type.
     */
    @XmlElement
    private String interval;

    /**
     * Chart type, i.e. what chart should actually show
     * ( total / average values or something else).
     */
    @XmlElement
    private String type;

    /**
     *
     */
    @XmlElement
    private String[] hosts;

    /**
     * Producer name.
     */
    @XmlElement
    private String producer;

    /**
     * Stat name.
     */
    @XmlElement
    private String stat;

    /**
     * Value name.
     */
    @XmlElement
    private String value;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
