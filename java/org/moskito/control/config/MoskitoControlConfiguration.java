package org.moskito.control.config;

import org.apache.log4j.Logger;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

/**
 * Configuration holder class for MoSKito Control. The configuration of MoSKito control is located in moskitocontrol.json file in the classpath.
 *
 * @author lrosenberg
 * @since 26.02.13 18:50
 */
@ConfigureMe (name="moskitocontrol", allfields = true)
public class MoskitoControlConfiguration {

	/**
	 * Logger.
	 */
	private static Logger log = Logger.getLogger(MoskitoControlConfiguration.class);

	@Configure
	private ApplicationConfig[] applications;

	@Configure
	private ConnectorConfig[] connectors;

	/**
	 * Configuration of the updater. A default configuration is provided, so you don't need to overwrite it,
	 * except for tuning.
	 */
	@Configure
	private UpdaterConfig updater = new UpdaterConfig();

	public ApplicationConfig[] getApplications() {
		return applications;
	}

	public void setApplications(ApplicationConfig[] applications) {
		this.applications = applications;
	}

	public ConnectorConfig[] getConnectors() {
		return connectors;
	}

	public void setConnectors(ConnectorConfig[] connectors) {
		this.connectors = connectors;
	}

	public static final MoskitoControlConfiguration getConfiguration(){
		//TODO reuse instance later.
		return loadConfiguration();
	}

	static final MoskitoControlConfiguration loadConfiguration(){
		MoskitoControlConfiguration config = new MoskitoControlConfiguration();
		try {
			ConfigurationManager.INSTANCE.configure(config);
		}catch(IllegalArgumentException e){
			log.warn("can't find configuration - ensure you have moskitocontrol.json in the classpath");
		}
		return config;
	}

	public UpdaterConfig getUpdater() {
		return updater;
	}

	public void setUpdater(UpdaterConfig updater) {
		this.updater = updater;
	}

	public ApplicationConfig getApplication(String name){
		for (ApplicationConfig a : applications){
			if (a.getName().equals(name))
				return a;
		}
		throw new IllegalArgumentException("App with name "+name+" not found");
	}

}
