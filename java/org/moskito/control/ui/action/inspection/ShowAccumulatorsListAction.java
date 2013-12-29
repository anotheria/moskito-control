package org.moskito.control.ui.action.inspection;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import org.apache.commons.lang.StringUtils;
import org.moskito.control.connectors.response.ConnectorAccumulatorsNamesResponse;
import org.moskito.control.core.Application;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.Component;
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
        String applicationName = (String) req.getSession().getAttribute(ATT_APPLICATION);
        String componentName = req.getParameter("componentName");

        if (StringUtils.isEmpty(applicationName) || StringUtils.isEmpty(componentName)) {
            return mapping.error();
        }
        Application application = ApplicationRepository.getInstance().getApplication(applicationName);
        if (application == null) {
            return mapping.error();
        }
        Component component = application.getComponent(componentName);
        if (component == null) {
            return mapping.error();
        }

        ComponentInspectionDataProvider provider = new ComponentInspectionDataProvider();
        ConnectorAccumulatorsNamesResponse response = provider.provideAccumulatorsNames(application, component);
        if (response == null) {
            return mapping.error();
        }

        Collections.sort(response.getNames());
        req.setAttribute("accumulatorsNames", response.getNames());
        return mapping.success();
    }

}
