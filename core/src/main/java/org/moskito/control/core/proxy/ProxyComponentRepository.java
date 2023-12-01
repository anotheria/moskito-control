package org.moskito.control.core.proxy;

import org.moskito.control.common.HealthColor;
import org.moskito.control.config.ChartConfig;
import org.moskito.control.config.ProxyConfig;
import org.moskito.control.config.ViewConfig;
import org.moskito.control.core.Component;
import org.moskito.control.core.ComponentRepository;
import org.moskito.control.core.DataWidget;
import org.moskito.control.core.View;
import org.moskito.control.core.action.ComponentAction;
import org.moskito.control.core.chart.Chart;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ProxyComponentRepository implements ComponentRepository {

    private ProxyConfig config;

    private ProxyConnector connector;
    public ProxyComponentRepository(ProxyConfig config){
        this.config = config;
        connector = new ProxyConnector(config);
    }

    @Override
    public void addView(ViewConfig vc) {

    }

    @Override
    public List<View> getViews() {
        System.out.println(this+" getViews()");

        connector.updateData();

        List<View> viewsFromConnector = connector.getViews();
        if (viewsFromConnector == null || viewsFromConnector.isEmpty()) {
            return Collections.emptyList();
        }
        return viewsFromConnector;
    }

    @Override
    public List<Component> getComponents() {
        System.out.println(this+" getComponents()");
        return Collections.emptyList();
    }

    @Override
    public List<DataWidget> getDataWidgets() {
        System.out.println(this+" getDataWidgets()");
        return Collections.emptyList();
    }

    @Override
    public Component getComponent(String componentName) {
        System.out.println(this + " getComponent(" + componentName + ")");
        return null;
    }

    @Override
    public void addComponent(Component component) {

    }

    @Override
    public void addComponentAction(ComponentAction action) {

    }

    @Override
    public List<ComponentAction> getComponentActions(String componentName) {
        return null;
    }

    @Override
    public ComponentAction getComponentAction(String componentName, String actionName) {
        return null;
    }

    @Override
    public void addChart(ChartConfig cc) {

    }

    @Override
    public View getView(String name) {
        return connector.getView(name);
    }

    @Override
    public List<Chart> getCharts() {
        return null;
    }

    @Override
    public long getLastStatusUpdaterRun() {
        return 0;
    }

    @Override
    public long getLastChartUpdaterRun() {
        return 0;
    }

    @Override
    public long getLastStatusUpdaterSuccess() {
        return 0;
    }

    @Override
    public long getLastChartUpdaterSuccess() {
        return 0;
    }

    @Override
    public long getStatusUpdaterRunCount() {
        return 0;
    }

    @Override
    public long getChartUpdaterRunCount() {
        return 0;
    }

    @Override
    public long getStatusUpdaterSuccessCount() {
        return 0;
    }

    @Override
    public long getChartUpdaterSuccessCount() {
        return 0;
    }

    @Override
    public HealthColor getWorstHealthStatus() {
        return null;
    }

    @Override
    public HealthColor getWorstHealthStatus(List<Component> components) {
        return null;
    }

    @Override
    public void removeComponent(String name) {

    }

    @Override
    public void removeChart(String name) {

    }

    @Override
    public void removeView(String name) {

    }

    public String toString(){
        return "ProxyComponentRepository "+ config;
    }
}
