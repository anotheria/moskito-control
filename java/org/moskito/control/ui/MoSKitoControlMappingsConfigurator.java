package org.moskito.control.ui;

import net.anotheria.maf.action.ActionForward;
import net.anotheria.maf.action.ActionMappings;
import net.anotheria.maf.action.ActionMappingsConfigurator;
import net.anotheria.maf.action.CommandRedirect;
import org.moskito.control.ui.action.MainViewAction;
import org.moskito.control.ui.action.SelectApplicationAction;
import org.moskito.control.ui.action.SelectCategoryAction;
import org.moskito.control.ui.action.ShowConfigurationAction;
import org.moskito.control.ui.action.SwitchChartsAction;
import org.moskito.control.ui.action.SwitchHistoryAction;
import org.moskito.control.ui.action.SwitchStatusAction;
import org.moskito.control.ui.action.SwitchTvAction;

/**
 * Mappings for MoSKito Control Actions.
 *
 * @author lrosenberg
 * @since 01.04.13 13:48
 */
public class MoSKitoControlMappingsConfigurator implements ActionMappingsConfigurator {
	@Override
	public void configureActionMappings(ActionMappings actionMappings) {
		actionMappings.addMapping("main", MainViewAction.class,
				new ActionForward("success", "/org/moskito/control/ui/jsp/MainView.jsp")
		);

		actionMappings.addMapping("setApplication", SelectApplicationAction.class,
			new CommandRedirect("redirect", "main", 302)
		);
		actionMappings.addMapping("setCategory", SelectCategoryAction.class,
				new CommandRedirect("redirect", "main", 302)
		);

		actionMappings.addMapping("switchHistory", SwitchHistoryAction.class,
				new CommandRedirect("redirect", "main", 302)
		);
		actionMappings.addMapping("switchCharts", SwitchChartsAction.class,
				new CommandRedirect("redirect", "main", 302)
		);
		actionMappings.addMapping("switchStatus", SwitchStatusAction.class,
				new CommandRedirect("redirect", "main", 302)
		);
		actionMappings.addMapping("switchTv", SwitchTvAction.class,
				new CommandRedirect("redirect", "main", 302)
		);


		actionMappings.addMapping("config", ShowConfigurationAction.class);


	}
}
