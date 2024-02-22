package org.moskito.control.ui.action.inspection;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;

import net.anotheria.util.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.moskito.control.connectors.ConnectorException;
import org.moskito.control.connectors.response.ConnectorThresholdsResponse;
import org.moskito.control.core.Repository;
import org.moskito.control.core.Component;
import org.moskito.control.core.inspection.ComponentInspectionDataProvider;
import org.moskito.control.common.ThresholdDataItem;
import org.moskito.control.ui.action.BaseMoSKitoControlAction;
import org.moskito.control.ui.bean.ThresholdBean;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

/**
 * Action for ajax-call to show thresholds of component.
 *
 * @author Vladyslav Bezuhlyi
 */
public class ShowThresholdsAction extends BaseMoSKitoControlAction {

    @Override
    public ActionCommand execute(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {
        String componentName = req.getParameter("componentName");

        ConnectorThresholdsResponse response = new ConnectorThresholdsResponse();

        if (StringUtils.isEmpty(componentName)) {
            return mapping.error();
        }

        Component component;

        try {
            component = Repository.getInstance().getComponent(componentName);
        }
        catch (IllegalArgumentException e){
            return mapping.error();
        }

        try {
            ComponentInspectionDataProvider provider = new ComponentInspectionDataProvider();
            response = provider.provideThresholds(component);
        }
        catch (ConnectorException | IllegalStateException ex) {
            return mapping.error();
        }

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
