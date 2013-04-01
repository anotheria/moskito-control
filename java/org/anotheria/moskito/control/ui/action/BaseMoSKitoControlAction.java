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

	}

	protected String getCurrentApplicationName(HttpServletRequest req){
		return (String)req.getSession().getAttribute(ATT_APPLICATION);
	}
}
