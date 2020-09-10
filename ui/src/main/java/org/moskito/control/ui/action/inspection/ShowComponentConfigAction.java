package org.moskito.control.ui.action.inspection;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import org.apache.commons.lang3.StringUtils;
import org.moskito.control.connectors.response.ConnectorConfigResponse;
import org.moskito.control.core.Component;
import org.moskito.control.core.ComponentRepository;
import org.moskito.control.core.inspection.ComponentInspectionDataProvider;
import org.moskito.control.ui.action.BaseMoSKitoControlAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowComponentConfigAction extends BaseMoSKitoControlAction {
    @Override
    public ActionCommand execute(ActionMapping actionMapping, FormBean formBean, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String componentName = request.getParameter("componentName");
        if (StringUtils.isEmpty(componentName)) {
            return actionMapping.error();
        }

        Component component = ComponentRepository.getInstance().getComponent(componentName);
        if (component == null) {
            return actionMapping.error();
        }

        ComponentInspectionDataProvider provider = new ComponentInspectionDataProvider();
        ConnectorConfigResponse connectorConfigResponse = provider.provideConfig(component);

        if (connectorConfigResponse == null) {
            return actionMapping.error();
        }

        request.setAttribute("componentConfig", connectorConfigResponse.getConfig());
        return actionMapping.success();
    }
}
