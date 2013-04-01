package org.anotheria.moskito.control.ui;

import net.anotheria.maf.action.ActionForward;
import net.anotheria.maf.action.ActionMappings;
import net.anotheria.maf.action.ActionMappingsConfigurator;
import net.anotheria.maf.action.CommandRedirect;
import org.anotheria.moskito.control.ui.action.MainViewAction;
import org.anotheria.moskito.control.ui.action.SelectApplicationAction;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 01.04.13 13:48
 */
public class MoSKitoControlMappingsConfigurator implements ActionMappingsConfigurator {
	@Override
	public void configureActionMappings(ActionMappings actionMappings) {
		actionMappings.addMapping("main", MainViewAction.class,
				new ActionForward("success", "/org/anotheria/moskito/control/ui/jsp/MainView.jsp")
		);

		actionMappings.addMapping("setApplication", SelectApplicationAction.class,
			new CommandRedirect("redirect", "main", 302)
		);

	}
}
