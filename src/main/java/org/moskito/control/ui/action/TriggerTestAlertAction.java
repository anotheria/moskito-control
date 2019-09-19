package org.moskito.control.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import org.moskito.control.core.Component;
import org.moskito.control.core.ComponentRepository;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.status.Status;
import org.moskito.control.core.status.StatusChangeEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This action triggers a component alert for testing of notifications.
 *
 * @author lrosenberg
 * @since 2019-09-19 11:41
 */
public class TriggerTestAlertAction extends BaseMoSKitoControlAction{
	@Override
	public ActionCommand execute(ActionMapping actionMapping, FormBean formBean, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

		String componentName = httpServletRequest.getParameter("component");
		String oldStatusString = httpServletRequest.getParameter("old");
		String newStatusString = httpServletRequest.getParameter("new");
		String newStatusMessage = httpServletRequest.getParameter("message");

		HealthColor oldColor = oldStatusString == null ? HealthColor.NONE : HealthColor.forName(oldStatusString);
		HealthColor newColor = newStatusString == null ? HealthColor.NONE : HealthColor.forName(newStatusString);
		Component c = ComponentRepository.getInstance().getComponent(componentName);
		StatusChangeEvent event = new StatusChangeEvent(
			c,
			new Status(oldColor, ""),
			new Status(newColor, "Test alert: "+newStatusMessage),
			System.currentTimeMillis()
		);

		ComponentRepository.getInstance().getEventsDispatcher().addStatusChange(event);
		return null;
	}
}
