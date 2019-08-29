package org.moskito.control.ui.action.inspection;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import org.apache.commons.lang.StringUtils;
import org.moskito.control.connectors.ConnectorException;
import org.moskito.control.connectors.response.ConnectorAccumulatorsNamesResponse;
import org.moskito.control.core.Component;
import org.moskito.control.core.ComponentRepository;
import org.moskito.control.core.inspection.ComponentInspectionDataProvider;
import org.moskito.control.ui.action.BaseMoSKitoControlAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

/**
 * Action for ajax-call of accumulators view of component.
 *
 * @author Vladyslav Bezuhlyi
 */
public class ShowAccumulatorsListAction extends BaseMoSKitoControlAction {

    @Override
    public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) throws Exception {
        String componentName = req.getParameter("componentName");

        ConnectorAccumulatorsNamesResponse response = new ConnectorAccumulatorsNamesResponse();

        if (StringUtils.isEmpty(componentName)) {
            return mapping.error();
        }
        Component component = ComponentRepository.getInstance().getComponent(componentName);

        try {
            ComponentInspectionDataProvider provider = new ComponentInspectionDataProvider();
            response = provider.provideAccumulatorsNames(component);
        }
        catch (ConnectorException | IllegalStateException ex) {
            return mapping.error();
        }

        if (response == null) {
            return mapping.error();
        }

        Collections.sort(response.getNames());
        req.setAttribute("accumulatorsNames", response.getNames());
        return mapping.success();
    }

}
