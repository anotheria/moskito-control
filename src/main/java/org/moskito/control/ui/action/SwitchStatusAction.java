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
public class SwitchStatusAction extends BaseMoSKitoControlAction{
	@Override
	public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res){

		String status = req.getParameter("status");
		if (status!=null)
		if (status.equalsIgnoreCase("on"))
			setStatusOn(req);
		else
			setStatusOff(req);

		String statusBeta = req.getParameter("statusBeta");
		if (statusBeta!=null)
			if (statusBeta.equalsIgnoreCase("on"))
				setStatusBetaOn(req);
			else
				setStatusBetaOff(req);

		return mapping.redirect();
	}

}
