package org.moskito.control.ui.action.inspection;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import org.apache.commons.lang3.StringUtils;
import org.moskito.control.connectors.ConnectorException;
import org.moskito.control.connectors.response.ConnectorInformationResponse;
import org.moskito.control.core.Component;
import org.moskito.control.core.ComponentRepository;
import org.moskito.control.core.inspection.ComponentInspectionDataProvider;
import org.moskito.control.ui.action.BaseMoSKitoControlAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action for ajax-call to show connector information.
 *
 * @author strel
 */
public class ShowConnectorInformationAction extends BaseMoSKitoControlAction {

    @Override
    public ActionCommand execute(ActionMapping mapping,  HttpServletRequest req, HttpServletResponse res) throws Exception {


        String componentName = req.getParameter("componentName");
		ConnectorInformationResponse response = new ConnectorInformationResponse();
        if ( StringUtils.isEmpty(componentName)) {
            return mapping.error();
        }

        Component component = ComponentRepository.getInstance().getComponent(componentName);

        try {
            ComponentInspectionDataProvider provider = new ComponentInspectionDataProvider();
            response = provider.provideConnectorInformation(component);
        }
        catch (IllegalStateException | ConnectorException  ex) {
            return mapping.error();
        }

        if (response == null) {
            return mapping.error();
        }

        req.setAttribute("connectorInformation", response.getInfo());
        return mapping.success();
    }

}
