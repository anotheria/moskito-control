package org.anotheria.moskito.control.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 02.04.13 11:07
 */
public class SelectCategoryAction extends BaseMoSKitoControlAction{
	@Override
	public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) throws Exception {

		String category = req.getParameter("category");
		if (category!=null && category.length()>0)
			setCurrentCategoryName(req, category);

		return mapping.redirect();
	}
}
