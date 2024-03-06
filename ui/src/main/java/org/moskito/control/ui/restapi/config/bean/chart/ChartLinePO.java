package org.moskito.control.ui.restapi.config.bean.chart;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;
import org.moskito.control.config.ChartConfig;
import org.moskito.control.config.ChartLineConfig;

import java.util.Objects;


@Schema(description="Configuration for a chart line within a chart")
public class ChartLinePO {

    @Schema(description = "Name of the component")
    private String component;

    @Schema(description = "Name of the accumulator in the component")
    private String accumulator;


    @Schema(description = "Caption of this line")
    private String caption;

    @Schema(description = "Instead of naming a single component, you can use tags to select multiple components. The tags are comma separated. If you use tags, the component field is ignored.")
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
