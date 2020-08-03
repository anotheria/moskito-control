package org.moskito.control.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The action removes current category filter.
 *
 * @author strel
 */
public class ClearCategoryFilterAction extends BaseMoSKitoControlAction {
    @Override
    public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) {
        clearCurrentCategoryName(req);
        return mapping.redirect();
    }
}
