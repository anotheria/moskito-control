package org.moskito.control.core;

import org.moskito.control.common.HealthColor;
import org.moskito.control.config.*;
import org.moskito.control.core.action.ComponentAction;
import org.moskito.control.core.chart.Chart;

import java.util.*;

public interface ComponentRepository {

    void addView(ViewConfig vc) ;

    List<View> getViews();

    List<Component> getComponents();

    List<DataWidget> getDataWidgets();

    Component getComponent(String componentName);

    void addComponent(Component component);

    void addComponentAction(ComponentAction action);

    List<ComponentAction> getComponentActions(String componentName) ;

    ComponentAction getComponentAction(String componentName, String actionName) ;


    void addChart(ChartConfig cc);

    View getView(String name) ;

    List<Chart> getCharts();



    long getLastStatusUpdaterRun();

    long getLastChartUpdaterRun() ;


    long getLastStatusUpdaterSuccess();

    long getLastChartUpdaterSuccess();

    long getStatusUpdaterRunCount();

    long getChartUpdaterRunCount();

    long getStatusUpdaterSuccessCount();

    long getChartUpdaterSuccessCount();

    /**
     * Returns the worst status of an application component, which is the worst status of the application.
     *
     * @return worst status of this application
     */
    HealthColor getWorstHealthStatus();


    HealthColor getWorstHealthStatus(List<Component> components) ;

    void removeComponent(String name);


    void removeChart(String name);

    void removeView(String name);


}
