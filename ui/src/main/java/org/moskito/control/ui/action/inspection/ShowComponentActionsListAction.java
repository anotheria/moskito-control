package org.moskito.control.ui.action.inspection;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import net.anotheria.util.StringUtils;
import org.moskito.control.core.ComponentRepository;
import org.moskito.control.core.action.ComponentAction;
import org.moskito.control.ui.action.BaseMoSKitoControlAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Action for ajax-call to show actions information.
 *
 * @author strel
 */
public class ShowComponentActionsListAction extends BaseMoSKitoControlAction {

    @Override
    public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) throws Exception {

        String componentName = req.getParameter("componentName");
        if (StringUtils.isEmpty(componentName)) {
            return mapping.error();
        }

        List<ComponentAction> componentActions = ComponentRepository.getInstance().getComponentActions(componentName);
        if (componentActions == null || componentActions.isEmpty()) {
            return mapping.error();
        }
        req.setAttribute("actions", componentActions);
        return mapping.success();
    }

}
