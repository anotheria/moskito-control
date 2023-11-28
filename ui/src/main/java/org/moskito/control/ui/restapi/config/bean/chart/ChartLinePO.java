package org.moskito.control.ui.restapi.config.bean.chart;

import com.google.gson.annotations.SerializedName;
import org.moskito.control.config.ChartConfig;
import org.moskito.control.config.ChartLineConfig;

import java.util.Objects;

public class ChartLinePO {

    private String component;

    private String accumulator;


    private String caption;

    private String componentTags;

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getAccumulator() {
        return accumulator;
    }

    public void setAccumulator(String accumulator) {
        this.accumulator = accumulator;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getComponentTags() {
        return componentTags;
    }

    public void setComponentTags(String componentTags) {
        this.componentTags = componentTags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChartLinePO that = (ChartLinePO) o;
        return Objects.equals(component, that.component) && Objects.equals(accumulator, that.accumulator) && Objects.equals(caption, that.caption) && Objects.equals(componentTags, that.componentTags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(component, accumulator, caption, componentTags);
    }

    @Override
    public String toString() {
        return "ChartLinePO{" +
                "component='" + component + '\'' +
                ", accumulator='" + accumulator + '\'' +
                ", caption='" + caption + '\'' +
                ", componentTags='" + componentTags + '\'' +
                '}';
    }

    public ChartLineConfig toChartLineConfig(){
        ChartLineConfig chartLine = new ChartLineConfig();
        chartLine.setComponent(this.getComponent());
        chartLine.setAccumulator(this.getAccumulator());
        chartLine.setCaption(this.getCaption());
        chartLine.setComponentTags(this.getComponentTags());

        return chartLine;
    }
}
