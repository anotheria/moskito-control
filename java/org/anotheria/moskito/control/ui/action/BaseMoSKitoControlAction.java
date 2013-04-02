package org.anotheria.moskito.control.ui.action;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 01.04.13 13:45
 */
public abstract class BaseMoSKitoControlAction implements Action {

	public static final String ATT_APPLICATION = "application";
	public static final String ATT_CATEGORY = "category";

	public static final String VALUE_ALL_CATEGORIES = "All Categories";

	@Override
	public void preProcess(ActionMapping actionMapping, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void postProcess(ActionMapping actionMapping, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	protected void setCurrentApplicationName(HttpServletRequest req, String application){
		req.getSession().setAttribute(ATT_APPLICATION, application);
		//reset other variables.
		req.getSession().removeAttribute(ATT_CATEGORY);

	}

	protected String getCurrentApplicationName(HttpServletRequest req){
		return (String)req.getSession().getAttribute(ATT_APPLICATION);
	}

	protected void setCurrentCategoryName(HttpServletRequest req, String categoryName){
		if (categoryName.equals(VALUE_ALL_CATEGORIES))
			categoryName = "";
		req.getSession().setAttribute(ATT_CATEGORY, categoryName);
	}

	protected String getCurrentCategoryName(HttpServletRequest req){
		return (String)req.getSession().getAttribute(ATT_CATEGORY);
	}
}
