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

    @XmlElement
    private List<MoskitoAnalyzeChartLineBean> lines;

    /**
     * Start date in milliseconds.
     */
    @XmlElement
    private long startDate;

    /**
     * End date in milliseconds.
     */
    @XmlElement
    private long endDate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<MoskitoAnalyzeChartLineBean> getLines() {
        return lines;
    }

    public void setLines(List<MoskitoAnalyzeChartLineBean> lines) {
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
