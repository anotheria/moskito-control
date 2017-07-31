package org.moskito.control.config;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

/**
 * General configuration for MoSKito-Analyze application access.
 * @author strel
 */
@ConfigureMe
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2", "EI_EXPOSE_REP"}, justification = "This is the way configureMe works, it provides beans for access")
public class MoskitoAnalyzeConfig {

    /**
     * MoSKito-Analyze application URL.
     */
    @Configure
    private String url;

    /**
     * Array of hosts.
     */
    @Configure
    private String[] hosts;

    /**
     * Array of configured chart parameters to retrieve from
     * MoSKito-Analyze endpoint.
     */
    @Configure
    private MoskitoAnalyzeChartConfig[] charts;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String[] getHosts() {
        return hosts;
    }

    public void setHosts(String[] hosts) {
        this.hosts = hosts;
    }

    public MoskitoAnalyzeChartConfig[] getCharts() {
        return charts;
    }

    public void setCharts(MoskitoAnalyzeChartConfig[] charts) {
        this.charts = charts;
    }
}
