package org.moskito.control.config;

import com.google.gson.annotations.SerializedName;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

/**
 * TODO: Comment
 * @author strel
 */
@ConfigureMe
public class ProducerConfig {

    /**
     * Producer caption used as second part for chart caption.
     */
    @Configure
    @SerializedName("caption")
    private String caption;

    /**
     * Producer name.
     */
    @Configure
    @SerializedName("producer")
    private String producer;

    /**
     * Stat name.
     */
    @Configure
    @SerializedName("stat")
    private String stat;

    /**
     * Value name.
     */
    @Configure
    @SerializedName("value")
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
