package org.moskito.control.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The action switches the history on and off.
 *
 * @author lrosenberg
 * @since 15.06.13 23:50
 */
public class SwitchTvAction extends BaseMoSKitoControlAction{
	@Override
	public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) {

		String tv = req.getParameter("tv");
		if (tv!=null && tv.equalsIgnoreCase("on"))
			setTvOn(req);
		else
			setTvOff(req);

		return mapping.redirect();
	}

}
