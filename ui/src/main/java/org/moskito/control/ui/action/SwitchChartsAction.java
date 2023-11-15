package org.moskito.control.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The action switches the history on and off.
 *
 * @author lrosenberg
 * @since 15.06.13 23:50
 */
public class SwitchChartsAction extends BaseMoSKitoControlAction{
	@Override
	public ActionCommand execute(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) {

		String charts = req.getParameter("charts");
		if (charts!=null && charts.equalsIgnoreCase("on"))
			setChartsOn(req);
		else
			setChartsOff(req);

		return mapping.redirect();
	}

}
