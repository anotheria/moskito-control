package org.moskito.control.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * The action switches the history on and off.
 *
 * @author lrosenberg
 * @since 15.06.13 23:50
 */
public class SwitchHistoryAction extends BaseMoSKitoControlAction{
	@Override
	public ActionCommand execute(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) {

		String history = req.getParameter("history");
		if (history!=null && history.equalsIgnoreCase("on"))
			setHistoryOn(req);
		else
			setHistoryOff(req);

		return mapping.redirect();
	}

}
