package org.moskito.control.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The action select the application.
 *
 * @author lrosenberg
 * @since 01.04.13 23:39
 */
public class SelectApplicationAction extends BaseMoSKitoControlAction{
	@Override
	public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) {

		String application = req.getParameter("application");
		if (application!=null && application.length()>0)
			setCurrentApplicationName(req, application);

		return mapping.redirect();
	}
}
