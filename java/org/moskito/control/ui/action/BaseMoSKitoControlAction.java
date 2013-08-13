package org.moskito.control.ui.action;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Base action for all ui actions.
 *
 * @author lrosenberg
 * @since 01.04.13 13:45
 */
public abstract class BaseMoSKitoControlAction implements Action {

	/**
	 * Name of the currently selected application in the session.
	 */
	public static final String ATT_APPLICATION = "application";
	/**
	 * Name of the currently selected category in the session.
	 */
	public static final String ATT_CATEGORY = "category";
	/**
	 * Name of the history state (on/off) in session.
	 */
	public static final String ATT_HISTORY_TOGGLE = "historyToggle";
	/**
	 * Name of the status state (on/off) in session.
	 */
	public static final String ATT_STATUS_TOGGLE = "statusToggle";
	/**
	 * Name of the charts state (on/off) in session.
	 */
	public static final String ATT_CHARTS_TOGGLE = "chartsToggle";

	/**
	 * Name of the tv toggle state (on/off) in session.
	 */
	public static final String ATT_TV_TOGGLE = "tvToggle";

	/**
	 * Constant for all categories value.
	 */
	public static final String VALUE_ALL_CATEGORIES = "All Categories";

	@Override
	public void preProcess(ActionMapping actionMapping, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)  {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void postProcess(ActionMapping actionMapping, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)  {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * Sets current application name.
	 * @param req
	 * @param application
	 */
	protected void setCurrentApplicationName(HttpServletRequest req, String application){
		req.getSession().setAttribute(ATT_APPLICATION, application);
		//reset other variables.
		req.getSession().removeAttribute(ATT_CATEGORY);

	}

	/**
	 * Returns currently selected application name.
	 * @param req
	 * @return
	 */
	protected String getCurrentApplicationName(HttpServletRequest req){
		return (String)req.getSession().getAttribute(ATT_APPLICATION);
	}

	/**
	 * Sets active category name.
	 * @param req
	 * @param categoryName
	 */
	protected void setCurrentCategoryName(HttpServletRequest req, String categoryName){
		if (categoryName.equals(VALUE_ALL_CATEGORIES))
			categoryName = "";
		req.getSession().setAttribute(ATT_CATEGORY, categoryName);
	}

	/**
	 * Returns currently selected category name.
	 * @param req
	 * @return
	 */
	protected String getCurrentCategoryName(HttpServletRequest req){
		String category = (String)req.getSession().getAttribute(ATT_CATEGORY);
		return category == null ? "" : category;
	}

	/**
	 * Returns true if the history is on for the current session.
	 * @param req
	 * @return
	 */
	protected boolean isHistoryOn(HttpServletRequest req){
		Boolean history = (Boolean)req.getSession().getAttribute(ATT_HISTORY_TOGGLE);
		//history is on by default - first request will put the attribute in session, cause this attribute is checked by the view.
		if (history==null)
			setHistoryOn(req);

		return history==null || history==Boolean.TRUE;
	}

	/**
	 * Returns true if the chart widget is on.
	 * @param req
	 * @return
	 */
	protected boolean areChartsOn(HttpServletRequest req){
		Boolean charts = (Boolean)req.getSession().getAttribute(ATT_CHARTS_TOGGLE);
		//charts is on by default - first request will put the attribute in session, cause this attribute is checked by the view.
		if (charts==null)
			setChartsOn(req);

		return charts==null || charts==Boolean.TRUE;
	}

	/**
	 * Returns true if the status widget is on.
	 * @param req
	 * @return
	 */
	protected boolean isStatusOn(HttpServletRequest req){
		Boolean status = (Boolean)req.getSession().getAttribute(ATT_STATUS_TOGGLE);
		//charts is on by default - first request will put the attribute in session, cause this attribute is checked by the view.
		if (status==null)
			setStatusOn(req);

		return status==null || status==Boolean.TRUE;
	}

	protected void setHistoryOn(HttpServletRequest req){
		req.getSession().setAttribute(ATT_HISTORY_TOGGLE, Boolean.TRUE);
	}

	protected void setHistoryOff(HttpServletRequest req){
		req.getSession().setAttribute(ATT_HISTORY_TOGGLE, Boolean.FALSE);
	}
	protected void setChartsOn(HttpServletRequest req){
		req.getSession().setAttribute(ATT_CHARTS_TOGGLE, Boolean.TRUE);
	}

	protected void setChartsOff(HttpServletRequest req){
		req.getSession().setAttribute(ATT_CHARTS_TOGGLE, Boolean.FALSE);
	}

	protected void setTvOn(HttpServletRequest req){
		req.getSession().setAttribute(ATT_TV_TOGGLE, Boolean.TRUE);
	}

	protected void setTvOff(HttpServletRequest req){
		req.getSession().setAttribute(ATT_TV_TOGGLE, Boolean.FALSE);
	}

	protected void setStatusOn(HttpServletRequest req){
		req.getSession().setAttribute(ATT_STATUS_TOGGLE, Boolean.TRUE);
	}

	protected void setStatusOff(HttpServletRequest req){
		req.getSession().setAttribute(ATT_STATUS_TOGGLE, Boolean.FALSE);
	}

}
