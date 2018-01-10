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

    @Configure
    @SerializedName("@lines")
    private MoskitoAnalyzeChartLineConfig[] lines;

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

    public MoskitoAnalyzeChartLineConfig[] getLines() {
        return lines;
    }

    public void setLines(MoskitoAnalyzeChartLineConfig[] lines) {
        this.lines = lines;
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
