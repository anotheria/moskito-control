package org.moskito.control.core.proxy;

import org.moskito.control.common.HealthColor;
import org.moskito.control.config.ProxyConfig;
import org.moskito.control.core.Component;
import org.moskito.control.core.DataWidget;
import org.moskito.control.core.View;
import org.moskito.control.core.chart.Chart;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ProxiedView extends View {

    private ProxyConfig config;

    private HealthColor proxiedColour = HealthColor.GREEN;

    private List<Component> components = new LinkedList<>();

    /**
     * Creates a new view with given name. Name is used to select a view and to present the view in the top navigation.
     *
     * @param aName
     */
    public ProxiedView(String aName, ProxyConfig config) {
        super(aName);
        this.config = config;
    }

    public String getName(){
        return config.getPrefix() + ":" + super.getName();
    }

    @Override
    public HealthColor getWorstHealthStatus() {
        return proxiedColour;
    }

    public void setProxiedColour(HealthColor proxiedColour) {
        this.proxiedColour = proxiedColour;
    }

    public void addComponent(ProxiedComponent c) {
        components.add(c);
    }

    @Override
    public List<Component> getComponents() {
        return components;
    }

    @Override
    public List<Chart> getCharts() {
        return Collections.emptyList();
    }

    @Override
    public List<DataWidget> getDataWidgets() {
        return Collections.emptyList();
    }
}
