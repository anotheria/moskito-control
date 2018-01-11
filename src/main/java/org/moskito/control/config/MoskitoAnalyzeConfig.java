package org.moskito.control.config;

import com.google.gson.annotations.SerializedName;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.lang.ArrayUtils;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.slf4j.LoggerFactory;

/**
 * General configuration for MoSKito-Analyze application access.
 * @author strel
 */
@ConfigureMe( name = "moskito-analyze", allfields = true)
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2", "EI_EXPOSE_REP"}, justification = "This is the way configureMe works, it provides beans for access")
public class MoskitoAnalyzeConfig {

    /**
     * MoSKito-Analyze application URL.
     */
    @Configure
    @SerializedName("url")
    private String url;

    @Configure
    @SerializedName("@components")
    private String[] components;

    /**
     * Array of configured chart parameters to retrieve from
     * MoSKito-Analyze endpoint.
     */
    @Configure
    @SerializedName("@charts")
    private MoskitoAnalyzeChartConfig[] charts;

    /**
     * Monitor object.
     */
    private static final Object monitor = new Object();

    /**
     * {@link MoskitoAnalyzeConfig} instance
     */
    private static MoskitoAnalyzeConfig instance;


    public static MoskitoAnalyzeConfig getInstance() {
        if (instance != null)
            return instance;
        synchronized (monitor) {
            if (instance != null)
                return instance;
            instance = new MoskitoAnalyzeConfig();
            try {
                ConfigurationManager.INSTANCE.configure(instance);
            } catch (Exception e) {
                LoggerFactory.getLogger(MoskitoAnalyzeConfig.class).warn("Configuration failed. Relying on defaults. " + e.getMessage());
            }
            return instance;
        }
    }

    private MoskitoAnalyzeConfig(){
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String[] getComponents() {
        return components;
    }

    public void setComponents(String[] components) {
        this.components = components;
    }

    public MoskitoAnalyzeChartConfig[] getCharts() {
        return charts;
    }

    public void setCharts(MoskitoAnalyzeChartConfig[] charts) {
        this.charts = charts;
    }

    public MoskitoAnalyzeChartConfig getChartByName(String name) {
        for (MoskitoAnalyzeChartConfig chartConfig : charts) {
            if (chartConfig.getName().equalsIgnoreCase(name)) {
                return chartConfig;
            }
        }

        return null;
    }

    public boolean updateChartById(String id, MoskitoAnalyzeChartConfig chart) {
        int chartIndex = getChartIndexById(id);

        if (chartIndex == -1) {
            return false;
        }

        charts[chartIndex] = chart;

        return true;
    }

    public boolean removeChartById(String id) {
        int chartIndex = getChartIndexById(id);

        if (chartIndex == -1) {
            return false;
        }

        charts = (MoskitoAnalyzeChartConfig[]) ArrayUtils.remove(charts, chartIndex);
        return true;
    }

    public int getChartIndexById(String id) {
        for (int i = 0; i < charts.length; i++) {
            if (charts[i].getId().equalsIgnoreCase(id)) {
                return i;
            }
        }

        return -1;
    }
}
