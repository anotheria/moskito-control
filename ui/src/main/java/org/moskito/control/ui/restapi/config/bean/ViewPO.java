package org.moskito.control.ui.restapi.config.bean;

import org.moskito.control.config.ViewConfig;

import java.util.Arrays;
import java.util.Objects;

public class ViewPO {

    private String name;

    private String[] componentCategories;

    private String[] components;

    private String[] charts;

    private String[] chartTags;

    private String[] componentTags;

    private String[] widgets;

    private String[] widgetTags;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getComponentCategories() {
        return componentCategories;
    }

    public void setComponentCategories(String[] componentCategories) {
        this.componentCategories = componentCategories;
    }

    public String[] getComponents() {
        return components;
    }

    public void setComponents(String[] components) {
        this.components = components;
    }

    public String[] getCharts() {
        return charts;
    }

    public void setCharts(String[] charts) {
        this.charts = charts;
    }

    public String[] getChartTags() {
        return chartTags;
    }

    public void setChartTags(String[] chartTags) {
        this.chartTags = chartTags;
    }

    public String[] getComponentTags() {
        return componentTags;
    }

    public void setComponentTags(String[] componentTags) {
        this.componentTags = componentTags;
    }

    public String[] getWidgets() {
        return widgets;
    }

    public void setWidgets(String[] widgets) {
        this.widgets = widgets;
    }

    public String[] getWidgetTags() {
        return widgetTags;
    }

    public void setWidgetTags(String[] widgetTags) {
        this.widgetTags = widgetTags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewPO viewPO = (ViewPO) o;
        return Objects.equals(name, viewPO.name) && Arrays.equals(componentCategories, viewPO.componentCategories) && Arrays.equals(components, viewPO.components) && Arrays.equals(charts, viewPO.charts) && Arrays.equals(chartTags, viewPO.chartTags) && Arrays.equals(componentTags, viewPO.componentTags) && Arrays.equals(widgets, viewPO.widgets) && Arrays.equals(widgetTags, viewPO.widgetTags);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name);
        result = 31 * result + Arrays.hashCode(componentCategories);
        result = 31 * result + Arrays.hashCode(components);
        result = 31 * result + Arrays.hashCode(charts);
        result = 31 * result + Arrays.hashCode(chartTags);
        result = 31 * result + Arrays.hashCode(componentTags);
        result = 31 * result + Arrays.hashCode(widgets);
        result = 31 * result + Arrays.hashCode(widgetTags);
        return result;
    }

    @Override
    public String toString() {
        return "ViewPO{" +
                "name='" + name + '\'' +
                ", componentCategories=" + Arrays.toString(componentCategories) +
                ", components=" + Arrays.toString(components) +
                ", charts=" + Arrays.toString(charts) +
                ", chartTags=" + Arrays.toString(chartTags) +
                ", componentTags=" + Arrays.toString(componentTags) +
                ", widgets=" + Arrays.toString(widgets) +
                ", widgetTags=" + Arrays.toString(widgetTags) +
                '}';
    }

    public ViewConfig toViewConfig(){
        ViewConfig view = new ViewConfig();
        view.setName(this.getName());
        view.setComponentCategories(this.getComponentCategories());
        view.setComponents(this.getComponents());
        view.setCharts(this.getCharts());
        view.setChartTags(this.getChartTags());
        view.setComponentTags(this.getComponentTags());
        view.setWidgets(this.getWidgets());
        view.setWidgetTags(this.getWidgetTags());
        return view;
    }
}
