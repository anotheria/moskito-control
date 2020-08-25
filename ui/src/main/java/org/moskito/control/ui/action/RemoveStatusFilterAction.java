package org.moskito.control.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import org.apache.commons.lang3.StringUtils;
import org.moskito.control.common.HealthColor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The action removes color filter.
 *
 * @author strel
 */
public class RemoveStatusFilterAction extends BaseMoSKitoControlAction {
    @Override
    public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) {

        String color = req.getParameter(ATT_COLOR);
        if (!StringUtils.isEmpty(color))
            removeStatusFilter(req, HealthColor.forName(color));

        return mapping.redirect();
    }
}
