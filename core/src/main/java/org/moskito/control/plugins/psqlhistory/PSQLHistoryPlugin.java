package org.moskito.control.plugins.psqlhistory;

import org.configureme.ConfigurationManager;
import org.moskito.control.core.history.StatusUpdateHistoryRepository;
import org.moskito.control.core.history.service.HistoryService;
import org.moskito.control.plugins.AbstractMoskitoControlPlugin;

public class PSQLHistoryPlugin extends AbstractMoskitoControlPlugin {
    private String configurationName;

    @Override
    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }

    @Override
    public void initialize() {
        PSQLHistoryPluginConfig config = new PSQLHistoryPluginConfig();
        ConfigurationManager.INSTANCE.configureAs(config, configurationName);

        HistoryService historyService = new PSQLHistoryService(config);
        StatusUpdateHistoryRepository.getInstance().setHistoryService(historyService);
    }
}
