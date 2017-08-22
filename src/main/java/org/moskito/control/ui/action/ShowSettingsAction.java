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
 * @since 18.06.13 13:36
 */
public class ShowSettingsAction extends BaseMoSKitoControlAction {


	@Override
	public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) throws Exception{

		String settings = req.getParameter("settings");
		if ("on".equalsIgnoreCase(settings))
			setSettingsOn(req);
		else
			setSettingsOff(req);

		return mapping.redirect();
	}
}
