package org.moskito.control.ui.restapi.config.bean.chart;
import org.moskito.control.config.ChartConfig;
import org.moskito.control.config.ChartLineConfig;

import java.util.Arrays;
import java.util.Objects;

public class ChartPO {

    private String name;

    private ChartLinePO[] lines;

    private String tags;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChartLinePO[] getLines() {
        return lines;
    }

    private ChartLineConfig[] getLineConfigs(){
        ChartLineConfig[] lineConfigs = new ChartLineConfig[lines.length];
        int i = 0;
        for(ChartLinePO po : lines){
            lineConfigs[i] = po.toChartLineConfig();
            i++;
        }
        return lineConfigs;
    }

    public void setLines(ChartLinePO[] lines) {
        this.lines = lines;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChartPO chartPO = (ChartPO) o;
        return Objects.equals(name, chartPO.name) && Arrays.equals(lines, chartPO.lines) && Objects.equals(tags, chartPO.tags);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, tags);
        result = 31 * result + Arrays.hashCode(lines);
        return result;
    }

    @Override
    public String toString() {
        return "ChartPO{" +
                "name='" + name + '\'' +
                ", lines=" + Arrays.toString(lines) +
                ", tags='" + tags + '\'' +
                '}';
    }

    public ChartConfig toChartConfig(){
        ChartConfig chart = new ChartConfig();
        chart.setName(this.getName());
        chart.setLines(this.getLineConfigs());
        chart.setTags(this.getTags());

        return chart;
    }


}
