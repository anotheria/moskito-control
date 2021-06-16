package org.moskito.control.plugins.monitoring.mail;

import org.moskito.control.config.ComponentConfig;
import org.moskito.control.config.ConnectorType;
import org.moskito.control.config.custom.CustomConfigurationProvider;
import org.moskito.control.config.datarepository.DataProcessingConfig;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ynikonchuk
 */
public class MonitoringMailConfigurationProvider implements CustomConfigurationProvider {

	private final MonitoringMailPluginConfig pluginConfig;

	public MonitoringMailConfigurationProvider(MonitoringMailPluginConfig aPluginConfig){
		pluginConfig = aPluginConfig;
	}
	
	@Override
	public DataProcessingConfig getDataProcessingConfig() {
		return new DataProcessingConfig();
	}

	@Override
	public List<ComponentConfig> getComponents() {
		LinkedList<ComponentConfig> ret = new LinkedList<>();
		MonitoringMailConfig[] targets = pluginConfig.getMailConfigs();
		for (MonitoringMailConfig mailConfig : targets){
			ComponentConfig componentConfig = new ComponentConfig();
			componentConfig.setCategory(pluginConfig.getCategoryName());
			componentConfig.setName(mailConfig.getName());
			componentConfig.setTags(pluginConfig.getTags());
			componentConfig.setConnectorType(ConnectorType.LOCALMOSKITO);
			ret.add(componentConfig);
		}

		return ret;
	}
}
