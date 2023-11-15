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
public class ShowConfigurationAction extends BaseMoSKitoControlAction{


	@Override
	public ActionCommand execute(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception{

		String config = req.getParameter("config");
		if ("on".equalsIgnoreCase(config))
			setConfigOn(req);
		else
			setConfigOff(req);

		return mapping.redirect();
	}
}
