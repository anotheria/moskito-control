package org.moskito.control.core;

import net.anotheria.util.StringUtils;
import org.configureme.sources.ConfigurationSource;
import org.configureme.sources.ConfigurationSourceKey;
import org.configureme.sources.ConfigurationSourceListener;
import org.configureme.sources.ConfigurationSourceRegistry;
import org.moskito.control.common.HealthColor;
import org.moskito.control.config.*;
import org.moskito.control.config.custom.CustomConfigurationProvider;
import org.moskito.control.config.datarepository.WidgetConfig;
import org.moskito.control.core.action.ComponentAction;
import org.moskito.control.core.chart.Chart;
import org.moskito.control.core.proxy.ProxyComponentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

/**
 * Manages components.
 *
 * @author lrosenberg
 * @since 01.04.13 23:08
 */
public final class Repository {

    private InternalComponentRepository internalComponentRepository;

    private List<ComponentRepository> componentRepositories = new LinkedList<>();

    private Map<String,ProxyComponentRepository> proxyRepositories = new HashMap<>();

    /**
     * Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(Repository.class);

    /**
     * Timestamp of the last status update.
     */
    private long lastStatusUpdaterRun;
    /**
     * Timestamp of the last chart data update.
     */
    private long lastChartUpdaterRun;

    /**
     * Timestamp of the last successful application status update.
     */
    private long lastStatusUpdaterSuccess;
    /**
     * Timestamp of the last successful chart data update.
     */
    private long lastChartUpdaterSuccess;

    /**
     * Number of the status updater runs.
     */
    private long statusUpdaterRunCount;
    /**
     * Number of the chart updater runs.
     */
    private long chartUpdaterRunCount;

    /**
     * Number of the successful status updater runs.
     */
    private long statusUpdaterSuccessCount;
    /**
     * Number of the successful chart updater runs.
     */
    private long chartUpdaterSuccessCount;

	/**
	 * Custom configuration providers allow plugins to modify configuration.
	 */
	private List<CustomConfigurationProvider> customConfigurationProviders = new CopyOnWriteArrayList<>();

    /**
     * Returns the singleton instance of the ComponentRepository.
     *
     * @return instance of the ComponentRepository
     */
    public static Repository getInstance() {
        return RepositoryInstanceHolder.instance;

    }

    /**
     * Creates a new repository.
     */
    private Repository() {

        internalComponentRepository = new InternalComponentRepository();
        componentRepositories.add(internalComponentRepository);

        MoskitoControlConfiguration configuration = MoskitoControlConfiguration.getConfiguration();
        ProxyConfig[] proxies = configuration.getProxies();
        if (proxies != null) {
            for (ProxyConfig proxyConfig : proxies) {
                System.out.println("Found proxy: "+proxyConfig);
                ProxyComponentRepository proxyRepository = new ProxyComponentRepository(proxyConfig);
                componentRepositories.add(proxyRepository);
                proxyRepositories.put(proxyConfig.getPrefix(), proxyRepository);
            }
        }
    }


    public void addView(ViewConfig vc) {
        internalComponentRepository.addView(vc);
    }

	public List<View> getViews() {
        LinkedList<View> ret = new LinkedList<>();
        for (ComponentRepository cr : componentRepositories){
            ret.addAll(cr.getViews());
        }
        return ret;
    }

    public List<Component> getComponents() {
        return internalComponentRepository.getComponents();
    }

    public List<DataWidget> getDataWidgets() {
        return internalComponentRepository.getDataWidgets();
    }

    public Component getComponent(String componentName) {
        for (ComponentRepository cr : componentRepositories){
            Component c = cr.getComponent(componentName);
            if (c!=null)
                return c;
        }
        throw new IllegalArgumentException("Component "+componentName+" not found");
    }

    public void addComponent(Component component) {
        internalComponentRepository.addComponent(component);
    }


    public List<ComponentAction> getComponentActions(String componentName) {
        return internalComponentRepository.getComponentActions(componentName);
    }

    public ComponentAction getComponentAction(String componentName, String actionName) {
        return internalComponentRepository.getComponentAction(componentName, actionName);
    }

    public void addChart(ChartConfig cc) {
        internalComponentRepository.addChart(cc);
    }


    public EventsDispatcher getEventsDispatcher() {
        return internalComponentRepository.getEventsDispatcher();
    }

    public View getView(String name) {
        for (ComponentRepository cr : componentRepositories){
            View v = cr.getView(name);
            System.out.println("CR "+cr+" returned view "+v+" for name "+name);
            if (v!=null)
                return v;
        }

        //if view is unknown, return the first one. Just for giggles. This should be rethought. TODO.
        return internalComponentRepository.getViews().get(0);
    }

    public List<Chart> getCharts() {
        return internalComponentRepository.getCharts();
    }


    /**
     * Singleton instance holder class.
     */
    private static class RepositoryInstanceHolder {
        /**
         * Singleton instance.
         */
        private static final Repository instance = new Repository();
    }

    public long getLastStatusUpdaterRun() {
        return lastStatusUpdaterRun;
    }

    public void setLastStatusUpdaterRun(long lastStatusUpdaterRun) {
        statusUpdaterRunCount++;
        this.lastStatusUpdaterRun = lastStatusUpdaterRun;
    }

    public long getLastChartUpdaterRun() {
        return lastChartUpdaterRun;
    }

    public void setLastChartUpdaterRun(long lastChartUpdaterRun) {
        chartUpdaterRunCount++;
        this.lastChartUpdaterRun = lastChartUpdaterRun;
    }

    public long getLastStatusUpdaterSuccess() {
        return lastStatusUpdaterSuccess;
    }

    public void setLastStatusUpdaterSuccess(long lastStatusUpdaterSuccess) {
        statusUpdaterSuccessCount++;
        this.lastStatusUpdaterSuccess = lastStatusUpdaterSuccess;
    }

    public long getLastChartUpdaterSuccess() {
        return lastChartUpdaterSuccess;
    }

    public void setLastChartUpdaterSuccess(long lastChartUpdaterSuccess) {
        chartUpdaterSuccessCount++;
        this.lastChartUpdaterSuccess = lastChartUpdaterSuccess;
    }

    public long getStatusUpdaterRunCount() {
        return statusUpdaterRunCount;
    }

    public long getChartUpdaterRunCount() {
        return chartUpdaterRunCount;
    }

    public long getStatusUpdaterSuccessCount() {
        return statusUpdaterSuccessCount;
    }

    public long getChartUpdaterSuccessCount() {
        return chartUpdaterSuccessCount;
    }

    /**
     * Returns the worst status of an application component, which is the worst status of the application.
     *
     * @return worst status of this application
     */
    public HealthColor getWorstHealthStatus() {
        HealthColor ret = HealthColor.GREEN;
        for (Component c : getComponents()) { //TODO revisit - iterate directly over hashmap
            if (c.getHealthColor().isWorse(ret))
                ret = c.getHealthColor();
        }
        return ret;
    }

    public HealthColor getWorstHealthStatus(List<Component> components) {
        HealthColor ret = HealthColor.GREEN;
        for (Component c : components) {
            if (c.getHealthColor().isWorse(ret))
                ret = c.getHealthColor();
        }
        return ret;
    }

	/**
	 * Adds a custom provider. To be used by a plugin.
	 */
	public void addCustomConfigurationProvider(CustomConfigurationProvider provider){
        internalComponentRepository.addCustomConfigurationProvider(provider); //TODO maybe this should be done directly bypassing the repository.
	}


    public void removeComponent(String name){
        internalComponentRepository.removeComponent(name);
    }

    public void removeChart(String name){
        internalComponentRepository.removeChart(name);
    }

    public void removeView(String name) {
       internalComponentRepository.removeView(name);
    }

}
