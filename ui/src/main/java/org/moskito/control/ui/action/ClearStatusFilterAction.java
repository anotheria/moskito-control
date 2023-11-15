package org.moskito.control.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The action removes all color filters.
 *
 * @author strel
 */
public class ClearStatusFilterAction extends BaseMoSKitoControlAction {
    @Override
    public ActionCommand execute(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) {
        clearStatusFilter(req);
        return mapping.redirect();
    }
}
