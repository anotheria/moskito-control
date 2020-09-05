package org.moskito.control.ui.action.inspection;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import net.anotheria.util.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.moskito.control.core.history.StatusUpdateHistoryItem;
import org.moskito.control.core.history.StatusUpdateHistoryRepository;
import org.moskito.control.ui.action.BaseMoSKitoControlAction;
import org.moskito.control.ui.bean.HistoryItemBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Action for ajax-call to show history of component.
 *
 * @author strel
 */
public class ShowHistoryAction extends BaseMoSKitoControlAction {

    @Override
    public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) {
        String componentName = req.getParameter("componentName");

        if (StringUtils.isEmpty(componentName)) {
            return mapping.error();
        }

		List<HistoryItemBean> historyItemBeans = StatusUpdateHistoryRepository.getInstance().getHistoryForComponent(componentName)
				.stream()
				.map(this::convert)
				.collect(Collectors.toList());

        req.setAttribute("history", historyItemBeans);
        return mapping.success();
    }

    private HistoryItemBean convert(StatusUpdateHistoryItem historyItem) {
		HistoryItemBean bean = new HistoryItemBean();
		bean.setTime(NumberUtils.makeISO8601TimestampString(historyItem.getTimestamp()));
		bean.setComponentName(historyItem.getComponent().getName());
		bean.setNewStatus(historyItem.getNewStatus().getHealth().name().toLowerCase());
		bean.setOldStatus(historyItem.getOldStatus().getHealth().name().toLowerCase());
		return bean;
	}
}
