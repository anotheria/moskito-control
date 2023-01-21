package org.moskito.control.plugins.escalation;

import org.configureme.ConfigurationManager;
import org.moskito.control.common.HealthColor;
import org.moskito.control.common.Status;
import org.moskito.control.core.Component;
import org.moskito.control.core.ComponentRepository;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.AbstractMoskitoControlPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.Configuration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * First attempt on creation an escalation plugin. Main idea is to escalate if the state of a component isn't improving. Right now, if a component goes purple and you missed it,
 * unless someone actively looking onto the screen, there will be no further notification. Escalation plugin will provide additional warnings if a component is in a bad state over configured amount of time.
 * The configuration is done via a file supplied as configuration name in parameters to the plugin configuration. Example configuration file is plugin-escalation.json in ui module. You can use it, it will provide
 * additional alerts after 10, 30 and 60 minutes.
 * This plugin is new and subject to change often (get additional functionality).
 * @author lrosenberg
 */
public class EscalationPlugin extends AbstractMoskitoControlPlugin {
    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(EscalationPlugin.class);
    /**
     * Configuration name. Example configuration is 'plugin-escalation'.
     */
    private String configurationName;

    private ScheduledExecutorService executorService;

    /**
     * Component cache.
     */
    private HashMap<String, ComponentStatusHolder> components;

    /**
     * Config.
     */
    private EscalationPluginConfig config;

    @Override
    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }

    @Override
    public void initialize() {
        super.initialize();

        components = new HashMap<>();

        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new ComponentChecker(), 10, 60, TimeUnit.SECONDS);

        config = new EscalationPluginConfig();
        try {
            ConfigurationManager.INSTANCE.configureAs(config, configurationName);
        }catch(Exception any){
            log.info("Escalation manager config not found, starting with default values (configured config name: "+configurationName);
        }

        log.info("Escalation plugin loaded, config is: "+config);
    }

    class ComponentChecker implements Runnable{
        public void run(){
            List<Component> components = ComponentRepository.getInstance().getComponents();
            for (Component c : components){
                if (c.getStatus().getHealth() == HealthColor.PURPLE){
                    notifyComponentPurple(c);
                }else{
                    notifyComponentOk(c);
                }

            }
            performPostRunCheck();

        }
    }

    @Override
    public void deInitialize() {
        super.deInitialize();

        executorService.shutdown();
    }

    private void notifyComponentPurple(Component component){
        ComponentStatusHolder holder = components.get(component.getName());
        if (holder == null ){
            holder = new ComponentStatusHolder(component.getName(), component.getStatus());
            components.put(holder.getComponentName(), holder);
        }else{
            holder.increaseTicks();
        }
        holder.setMessage(component.getStatus().getMessages().toString());
    }

    private void notifyComponentOk(Component component){
        //remove component if exists.
        components.remove(component.getName());
    }

    private void performPostRunCheck(){
        log.debug("PostRunCheck");
        for (Map.Entry<String,ComponentStatusHolder> entry : components.entrySet()){
            ComponentStatusHolder componentStatusHolder = entry.getValue();
            log.debug("Component in status "+componentStatusHolder);
            String escalationMessage = config.getMessageForTickCount(componentStatusHolder.getTicks());
            if (escalationMessage != null){
                Status newStatus = new Status(HealthColor.PURPLE, escalationMessage+" with "+componentStatusHolder.getMessage());
                StatusChangeEvent event = new StatusChangeEvent(
                        ComponentRepository.getInstance().getComponent(componentStatusHolder.getComponentName()),
                        componentStatusHolder.getStatus(),
                        newStatus,
                        System.currentTimeMillis()
                );
                ComponentRepository.getInstance().getEventsDispatcher().addStatusChange(event);
            }
        }
    }
}
