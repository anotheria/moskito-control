package org.moskito.control.config;

import com.google.gson.annotations.SerializedName;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.configureme.annotations.DontConfigure;

import java.util.Calendar;
import java.util.Date;

/**
 * Represents chart properties used in request to MoSKito-Analyze.
 * @author strel
 */
@ConfigureMe
public class MoskitoAnalyzeChartConfig {

    /**
     * Chart name.
     */
    @Configure
    @SerializedName("name")
    private String name;

    /**
     * Chart caption.
     */
    @Configure
    @SerializedName("caption")
    private String caption;

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
    @SerializedName("@components")
    private String[] components;

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

    /**
     * Start date in milliseconds.
     */
    @DontConfigure
    private long startDate;

    /**
     * End date in milliseconds.
     */
    @DontConfigure
    private long endDate;


    public MoskitoAnalyzeChartConfig() {
        Calendar dayStart = Calendar.getInstance();
        dayStart.set(Calendar.HOUR_OF_DAY, 0);
        dayStart.set(Calendar.MINUTE, 0);
        dayStart.set(Calendar.SECOND, 0);

        startDate = dayStart.getTimeInMillis();
        endDate = new Date().getTime();
    }


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

    public String[] getComponents() {
        return components;
    }

    public void setComponents(String[] components) {
        this.components = components;
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

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }
}
