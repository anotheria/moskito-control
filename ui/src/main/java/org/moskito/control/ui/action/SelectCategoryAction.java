package org.moskito.control.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The action selects the category.
 *
 * @author lrosenberg
 * @since 02.04.13 11:07
 */
public class SelectCategoryAction extends BaseMoSKitoControlAction{
	@Override
	public ActionCommand execute(ActionMapping mapping,  HttpServletRequest req, HttpServletResponse res) {

		String category = req.getParameter("category");
		if (category!=null && category.length()>0)
			setCurrentCategoryName(req, category);

		return mapping.redirect();
	}
}
