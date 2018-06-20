package org.moskito.control.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 04.06.18 16:39
 */
public class ShowDataRepositoryAction extends BaseMoSKitoControlAction {

    @Override
    public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) throws Exception {

        String config = req.getParameter("dataRepository");
        if ("on".equalsIgnoreCase(config))
            setDataRepositoryOn(req);
        else
            setDataRepositoryOff(req);

        return mapping.redirect();
    }
}
