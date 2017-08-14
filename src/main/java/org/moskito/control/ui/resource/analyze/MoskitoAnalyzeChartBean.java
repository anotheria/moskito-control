package org.moskito.control.ui.resource.analyze;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Represents chart parameters which will be requested.
 * @author strel
 */
@XmlRootElement
public class MoskitoAnalyzeChartBean {

    /**
     * Chart name used also as caption.
     */
    @XmlElement
    private String name;

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
     *
     */
    @XmlElement
    private List<MoskitoAnalyzeProducerBean> producers;


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

    public List<MoskitoAnalyzeProducerBean> getProducers() {
        return producers;
    }

    public void setProducers(List<MoskitoAnalyzeProducerBean> producers) {
        this.producers = producers;
    }
}
