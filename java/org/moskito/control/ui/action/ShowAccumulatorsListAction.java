package org.moskito.control.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import org.apache.commons.lang.StringUtils;
import org.moskito.control.connectors.response.ConnectorAccumulatorsNamesResponse;
import org.moskito.control.core.Application;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.Component;
import org.moskito.control.core.provider.AccumulatorsNamesDataProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        String id = req.getParameter("id");

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

        AccumulatorsNamesDataProvider provider = new AccumulatorsNamesDataProvider();
        ConnectorAccumulatorsNamesResponse response = provider.provideData(application, component);
        if (response == null) {
            return mapping.error();
        }

        req.setAttribute("accumulatorsNames", response.getNames());
        req.setAttribute("id", id);
        return mapping.success();
    }

}
