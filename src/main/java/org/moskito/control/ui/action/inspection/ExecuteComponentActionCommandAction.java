package org.moskito.control.ui.action.inspection;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import org.moskito.control.connectors.ActionType;
import org.moskito.control.core.ComponentRepository;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.action.ComponentAction;
import org.moskito.control.core.status.Status;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.ui.action.BaseMoSKitoControlAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Action for ajax-call to execute component action command.
 */
public class ExecuteComponentActionCommandAction extends BaseMoSKitoControlAction {
    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(ExecuteComponentActionCommandAction.class);

    @Override
    public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) throws Exception {
        String componentName = req.getParameter("componentName");
        String name = req.getParameter("name");
        ComponentAction componentAction = ComponentRepository.getInstance().getComponentAction(componentName, name);
        if (componentAction == null) {
            return mapping.error();
        }
        String command = componentAction.getCommand();
        ActionType type = componentAction.getType();
        StringBuilder commandExecuteResult = new StringBuilder();
        commandExecuteResult.append("Incoming parameters: command - \'").append(command).append("\'; type - ").append(type).append("<br/>");
        log.info("Incoming parameters: command - \'{}\'; type - {}", command, type);

        switch (type) {
            case SSH:
                commandExecuteResult.append(executeSshCommand(command));
                break;
        }
        req.setAttribute("commandExecuteResult", commandExecuteResult);
        Status status = new Status(HealthColor.NONE, "Action '" + name + "' executed");
        StatusChangeEvent event = new StatusChangeEvent(ComponentRepository.getInstance().getComponent(componentName), status, status, System.currentTimeMillis());
        ComponentRepository.getInstance().getEventsDispatcher().addStatusChange(event);
        return mapping.success();
    }

    private String executeSshCommand(String command) {
        StringBuilder ret = new StringBuilder();
        ret.append("Executing command - \'").append(command).append("\'<br/>");
        log.info("Executing command - \'{}\'", command);
        int index = command.indexOf(';');
        String host = command.substring(ActionType.SSH.name().length(), index).trim();
        String cmdCommands = command.substring(index + 1);
        try {
            ProcessBuilder pb = new ProcessBuilder(ActionType.SSH.name().toLowerCase(), host, cmdCommands);
            Process process = pb.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                ret.append(line).append("<br/>");
                log.info(line);
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            ret.append("Command - \'").append(command).append("\' failed with error : ").append(e.getMessage()).append("<br/>");
        }
        ret.append("Command - \'").append(command).append("\' executed.<br/><br/>");
        log.info("Command - \'{}\' executed.", command);
        return ret.toString();
    }
}
