package org.moskito.control.ui.action.inspection;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import net.anotheria.util.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.moskito.control.connectors.response.ConnectorThresholdsResponse;
import org.moskito.control.core.Application;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.Component;
import org.moskito.control.core.inspection.ComponentInspectionDataProvider;
import org.moskito.control.core.threshold.ThresholdDataItem;
import org.moskito.control.ui.action.BaseMoSKitoControlAction;
import org.moskito.control.ui.bean.ThresholdBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

/**
 * Action for ajax-call to show thresholds of component.
 *
 * @author Vladyslav Bezuhlyi
 */
public class ShowThresholdsAction extends BaseMoSKitoControlAction {

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
        ConnectorThresholdsResponse response = provider.provideThresholds(application, component);
        if (response == null) {
            return mapping.error();
        }

        LinkedList<ThresholdBean> thresholds = new LinkedList<ThresholdBean>();
        List<ThresholdDataItem> items = response.getItems();
        for (ThresholdDataItem item : items) {
            ThresholdBean threshold = new ThresholdBean();
            threshold.setName(item.getName());
            threshold.setStatus(item.getStatus().toString().toLowerCase());
            threshold.setLastValue(item.getLastValue());
            threshold.setStatusChangeTimestamp(NumberUtils.makeISO8601TimestampString(item.getStatusChangeTimestamp()));
            thresholds.add(threshold);
        }

        req.setAttribute("thresholds", thresholds);
        return mapping.success();
    }

}
