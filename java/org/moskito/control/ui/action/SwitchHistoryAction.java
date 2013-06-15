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
 * @since 15.06.13 23:50
 */
public class SwitchHistoryAction extends BaseMoSKitoControlAction{
	@Override
	public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) throws Exception {

		String history = req.getParameter("history");
		if (history!=null && history.equalsIgnoreCase("on"))
			setHistoryOn(req);
		else
			setHistoryOff(req);

		return mapping.redirect();
	}

}
