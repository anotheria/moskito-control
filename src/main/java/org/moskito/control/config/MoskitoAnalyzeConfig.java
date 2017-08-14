package org.moskito.control.config;

import com.google.gson.annotations.SerializedName;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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

    public MoskitoAnalyzeChartConfig[] getCharts() {
        return charts;
    }

    public void setCharts(MoskitoAnalyzeChartConfig[] charts) {
        this.charts = charts;
    }
}
