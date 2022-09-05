package org.moskito.control.config;

import com.google.gson.annotations.SerializedName;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.connectors.ActionType;

/**
 * Configuration of action.
 */
@ConfigureMe
@SuppressFBWarnings(value={"EI_EXPOSE_REP", "EI_EXPOSE_REP2"}, justification="Configureme configs are designed in the way, that they expose the arrays.")
public class ActionConfig {

    /**
     * Type of the action.
     */
    @Configure
    @SerializedName("type")
    private ActionType type;
    /**
     * Name of the action.
     */
    @Configure
    @SerializedName("name")
    private String name;

    /**
     * Component name.
     */
    @Configure
    @SerializedName("component")
    private String component;

    /**
     * Command.
     */
    @Configure
    @SerializedName("command")
    private String command;

    /**
     * Parameters.
     */
    @Configure
    @SerializedName("@parameters")
    private ActionParameter[] parameters;

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


    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public ActionParameter[] getParameters() {
        return parameters;
    }

    public void setParameters(ActionParameter[] parameters) {
        this.parameters = parameters;
    }
}
