package org.moskito.control.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * The action select the application.
 *
 * @author lrosenberg
 * @since 01.04.13 23:39
 */
public class SelectViewAction extends BaseMoSKitoControlAction{
	@Override
	public ActionCommand execute(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) {

		String view = req.getParameter("view");
		if (view!=null && view.length()>0)
			setCurrentViewName(req, view);

		return mapping.redirect();
	}
}
