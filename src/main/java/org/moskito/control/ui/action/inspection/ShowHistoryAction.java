package org.moskito.control.ui.action.inspection;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import net.anotheria.util.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.moskito.control.core.history.StatusUpdateHistoryItem;
import org.moskito.control.core.history.StatusUpdateHistoryRepository;
import org.moskito.control.ui.action.BaseMoSKitoControlAction;
import org.moskito.control.ui.bean.HistoryItemBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

/**
 * Action for ajax-call to show history of component.
 *
 * @author strel
 */
public class ShowHistoryAction extends BaseMoSKitoControlAction {

    @Override
    public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) {
        String applicationName = req.getParameter("applicationName");
        String componentName = req.getParameter("componentName");
        LinkedList<HistoryItemBean> historyItemBeans = new LinkedList<>();

        if (StringUtils.isEmpty(applicationName)) {
            applicationName = (String) req.getSession().getAttribute(ATT_APPLICATION);
        }

        if (StringUtils.isEmpty(applicationName) || StringUtils.isEmpty(componentName)) {
            return mapping.error();
        }

        if (!StringUtils.isEmpty(applicationName)) {
            List<StatusUpdateHistoryItem> historyItems = StatusUpdateHistoryRepository.getInstance().getHistoryForApplication(applicationName);
            for (StatusUpdateHistoryItem historyItem : historyItems) {
                if (componentName.equals(historyItem.getComponent().getName())) {
                    HistoryItemBean bean = new HistoryItemBean();
                    bean.setTime(NumberUtils.makeISO8601TimestampString(historyItem.getTimestamp()));
                    bean.setComponentName(historyItem.getComponent().getName());
                    bean.setNewStatus(historyItem.getNewStatus().getHealth().name().toLowerCase());
                    bean.setOldStatus(historyItem.getOldStatus().getHealth().name().toLowerCase());
                    historyItemBeans.add(bean);
                }
            }
        }

        req.setAttribute("history", historyItemBeans);
        return mapping.success();
    }

}
