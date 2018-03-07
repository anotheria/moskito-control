package org.moskito.control.ui.action;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionMapping;
import org.moskito.control.core.HealthColor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

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
	 * Name of the currently selected statistics color in the session.
	 */
	public static final String ATT_COLOR = "color";
	/**
	 * Name of the history state (on/off) in session.
	 */
	public static final String ATT_HISTORY_TOGGLE = "historyToggle";
	/**
	 * Name of the status state (on/off) in session.
	 */
	public static final String ATT_STATUS_TOGGLE = "statusToggle";
	/**
	 * Name of the new (beta) status state (on/off) in session.
	 */
	public static final String ATT_STATUS_TOGGLE_BETA = "statusBetaToggle";

	/**
	 * Name of the charts state (on/off) in session.
	 */
	public static final String ATT_CHARTS_TOGGLE = "chartsToggle";

	/**
	 * Name of the tv toggle state (on/off) in session.
	 */
	public static final String ATT_TV_TOGGLE = "tvToggle";

	/**
	 * Name of the config toggle state (on/off) in session.
	 */
	public static final String ATT_CONFIG_TOGGLE = "configToggle";

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

	}

	/**
	 * Returns currently selected application name.
	 * @param req
	 * @return current application name
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
	 * @return current category name
	 */
	protected String getCurrentCategoryName(HttpServletRequest req){
		String category = (String)req.getSession().getAttribute(ATT_CATEGORY);
		return category == null ? "" : category;
	}

	protected void clearCurrentCategoryName(HttpServletRequest req) {
		req.getSession().removeAttribute(ATT_CATEGORY);
	}

	protected void addStatusFilter(HttpServletRequest req, HealthColor color) {
		if (color == null || req == null) return;

		List<HealthColor> colorFilter = getStatusFilter(req);
		if (colorFilter == null)
			colorFilter = new ArrayList<>();

		colorFilter.add(color);
		setStatusFilter(req, colorFilter);
	}

	protected void removeStatusFilter(HttpServletRequest req, HealthColor color) {
		if (color == null || req == null) return;

		List<HealthColor> colorFilter = getStatusFilter(req);
		if (colorFilter == null)
			colorFilter = new ArrayList<>();

		colorFilter.remove(color);
		setStatusFilter(req, colorFilter);
	}

	protected void clearStatusFilter(HttpServletRequest req) {
		req.getSession().removeAttribute(ATT_COLOR);
	}

	protected List<HealthColor> getStatusFilter(HttpServletRequest req) {
		return req.getSession().getAttribute(ATT_COLOR) != null ?
				(List<HealthColor>) req.getSession().getAttribute(ATT_COLOR) : new ArrayList<>();
	}

	protected void setStatusFilter(HttpServletRequest req, List<HealthColor> colorFilter) {
		if (colorFilter == null || req == null) return;
		req.getSession().setAttribute(ATT_COLOR, colorFilter);
	}

	/**
	 * Returns true if the history is on for the current session.
	 * @param req
	 * @return true if the history is on, false - history is off
	 */
	protected boolean isHistoryOn(HttpServletRequest req){
		Boolean history = (Boolean)req.getSession().getAttribute(ATT_HISTORY_TOGGLE);
		//history is on by default - first request will put the attribute in session, cause this attribute is checked by the view.
		if (history==null)
			setHistoryOn(req);

		return history==null || Boolean.TRUE.equals(history);
	}

	/**
	 * Returns true if the chart widget is on.
	 * @param req
	 * @return true if the chart widget is on, false - off
	 */
	protected boolean areChartsOn(HttpServletRequest req){
		Boolean charts = (Boolean)req.getSession().getAttribute(ATT_CHARTS_TOGGLE);
		//charts is on by default - first request will put the attribute in session, cause this attribute is checked by the view.
		if (charts==null)
			setChartsOn(req);

		return charts==null || Boolean.TRUE.equals(charts);
	}

	/**
	 * Returns true if the status widget is on.
	 * @param req
	 * @return true if the status widget is on, false if status widget is off
	 */
	protected boolean isStatusOn(HttpServletRequest req){
		Boolean status = (Boolean)req.getSession().getAttribute(ATT_STATUS_TOGGLE);
		//charts is on by default - first request will put the attribute in session, cause this attribute is checked by the view.
		if (status==null)
			setStatusOn(req);

		return status==null || Boolean.TRUE.equals(status);
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

	protected void setStatusBetaOn(HttpServletRequest req){
		req.getSession().setAttribute(ATT_STATUS_TOGGLE_BETA, Boolean.TRUE);
	}

	protected void setStatusBetaOff(HttpServletRequest req){
		req.getSession().setAttribute(ATT_STATUS_TOGGLE_BETA, Boolean.FALSE);
	}

	protected void setConfigOn(HttpServletRequest req){
		req.getSession().setAttribute(ATT_CONFIG_TOGGLE, Boolean.TRUE);
	}

	protected void setConfigOff(HttpServletRequest req){
		req.getSession().setAttribute(ATT_CONFIG_TOGGLE, Boolean.FALSE);
	}

}
