package org.moskito.control.ui.resource.analyze;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * TODO: Comment
 * @author strel
 */
@XmlRootElement
public class MoskitoAnalyzeProducerBean {

    /**
     * Producer caption used as second part for chart caption.
     */
    @XmlElement
    private String caption;

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


    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
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
}
