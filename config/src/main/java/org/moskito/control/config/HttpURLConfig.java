package org.moskito.control.config;

import com.google.gson.annotations.SerializedName;
import net.anotheria.moskito.core.config.thresholds.GuardConfig;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@ConfigureMe(name = "http-url-connector")
public class HttpURLConfig {

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(MoskitoControlConfiguration.class);

    @Configure
    @SerializedName("@thresholdGuardConfig")
    private GuardConfig[] thresholdGuardConfig;

    public GuardConfig[] getThresholdGuardConfig() {
        return thresholdGuardConfig;
    }

    public void setThresholdGuardConfig(GuardConfig[] thresholdGuardConfig) {
        this.thresholdGuardConfig = thresholdGuardConfig;
    }

    /**
     * Returns the active configuration instance. The configuration object will update itself if the config is changed on disk.
     *
     * @return configuration instance
     */
    public static final HttpURLConfig getConfiguration() {
        return HttpURLConfig.HttpURLConfigHolder.instance;
    }


    /**
     * Loads a new configuration object from disk. This method is for unit testing.
     *
     * @return configuration object
     */
    public static final HttpURLConfig loadConfiguration() {
        HttpURLConfig config = new HttpURLConfig();
        try {
            ConfigurationManager.INSTANCE.configure(config);
        } catch (IllegalArgumentException e) {
            //ignored
        }
        return config;
    }

    /**
     * Holder class for singleton instance.
     */
    private static class HttpURLConfigHolder {
        /**
         * Singleton instance of the HttpURLConfig object.
         */
        static final HttpURLConfig instance;

        static {
            instance = new HttpURLConfig();
            try {
                ConfigurationManager.INSTANCE.configure(instance);
            } catch (IllegalArgumentException e) {
                log.warn("can't find configuration - ensure you have http-url-config.json in the classpath");
            }
        }
    }

    @Override
    public String toString() {
        return "HttpURLConfig{" +
                "thresholdGuardConfig=" + Arrays.toString(thresholdGuardConfig) +
                '}';
    }
}
