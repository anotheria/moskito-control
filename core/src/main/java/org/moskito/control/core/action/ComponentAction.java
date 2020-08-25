package org.moskito.control.core.action;

import org.json.JSONObject;
import org.moskito.control.config.ActionConfig;
import org.moskito.control.config.ActionParameter;
import org.moskito.control.connectors.ActionType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author asamoilich.
 */
public class ComponentAction {
    private ActionType type;
    private String name;
    private String componentName;
    private String command;
    private Map<String, String> parameters = new HashMap<>();

    /**
     * Creates a new component.
     */
    public ComponentAction(ActionConfig config) {
        type = config.getType();
        name = config.getName();
        componentName = config.getComponent();
        command = config.getCommand();
        ActionParameter[] actionParameters = config.getParameters();
        if (actionParameters != null) {
            for (ActionParameter parameter : actionParameters) {
                parameters.put(parameter.getKey(), parameter.getValue());
            }
        }
    }

    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getParametersJSON() {
        return new JSONObject(parameters).toString();
    }
}
