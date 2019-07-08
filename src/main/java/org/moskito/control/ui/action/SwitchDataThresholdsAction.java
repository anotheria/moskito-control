package org.moskito.control.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SwitchDataThresholdsAction extends BaseMoSKitoControlAction{
	@Override
	public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) {

		String config = req.getParameter("dataThresholds");
		if ("on".equalsIgnoreCase(config)) {
			setDataThresholdsOn(req);
		} else {
			setDataThresholdsOff(req);
		}

		return mapping.redirect();
	}
}
