package org.moskito.control.ui;

import net.anotheria.maf.action.ActionForward;
import net.anotheria.maf.action.ActionMappings;
import net.anotheria.maf.action.ActionMappingsConfigurator;
import net.anotheria.maf.action.CommandRedirect;
import org.moskito.control.ui.action.*;
import org.moskito.control.ui.action.inspection.ShowAccumulatorsChartsAction;
import org.moskito.control.ui.action.inspection.ShowAccumulatorsListAction;
import org.moskito.control.ui.action.inspection.ShowConnectorInformationAction;
import org.moskito.control.ui.action.inspection.ShowHistoryAction;
import org.moskito.control.ui.action.inspection.ShowThresholdsAction;

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
		actionMappings.addMapping("switchConfig", ShowConfigurationAction.class,
				new CommandRedirect("redirect", "main", 302)
		);

		actionMappings.addMapping("addStatusFilter", AddStatusFilterAction.class,
				new CommandRedirect("redirect", "main", 302)
		);
		actionMappings.addMapping("removeStatusFilter", RemoveStatusFilterAction.class,
				new CommandRedirect("redirect", "main", 302)
		);

		actionMappings.addMapping("clearStatusFilter", ClearStatusFilterAction.class,
				new CommandRedirect("redirect", "main", 302)
		);
		actionMappings.addMapping("clearCategoryFilter", ClearCategoryFilterAction.class,
				new CommandRedirect("redirect", "main", 302)
		);

		actionMappings.addMapping("muteNotifications", MuteNotificationsAction.class,
                new CommandRedirect("redirect", "main", 302)
        );
        actionMappings.addMapping("unmuteNotifications", UnmuteNotificationsAction.class,
                new CommandRedirect("redirect", "main", 302)
        );

        actionMappings.addMapping("thresholds", ShowThresholdsAction.class,
                new ActionForward("success", "/org/moskito/control/ui/jsp/inspection/Thresholds.jsp"),
                new ActionForward("error", "/org/moskito/control/ui/jsp/inspection/NoDataAvailable.jsp")
        );
        actionMappings.addMapping("accumulatorsList", ShowAccumulatorsListAction.class,
                new ActionForward("success", "/org/moskito/control/ui/jsp/inspection/AccumulatorsList.jsp"),
                new ActionForward("error", "/org/moskito/control/ui/jsp/inspection/NoDataAvailable.jsp")
        );
        actionMappings.addMapping("accumulatorsCharts", ShowAccumulatorsChartsAction.class,
                new ActionForward("success", "/org/moskito/control/ui/jsp/inspection/AccumulatorsCharts.jsp"),
                new ActionForward("error", "/org/moskito/control/ui/jsp/inspection/NoDataAvailable.jsp")
        );
		actionMappings.addMapping("connectorInformation", ShowConnectorInformationAction.class,
				new ActionForward("success", "/org/moskito/control/ui/jsp/inspection/ConnectorInformation.jsp"),
				new ActionForward("error", "/org/moskito/control/ui/jsp/inspection/NoDataAvailable.jsp")
		);
		actionMappings.addMapping("componentHistory", ShowHistoryAction.class,
				new ActionForward("success", "/org/moskito/control/ui/jsp/inspection/History.jsp"),
				new ActionForward("error", "/org/moskito/control/ui/jsp/inspection/NoDataAvailable.jsp")
		);

		//data repository
		actionMappings.addMapping("dataRepository", ShowDataRepositoryAction.class,
				new CommandRedirect("redirect", "main", 302)
		);

		//data thresholds
		actionMappings.addMapping("switchDataThresholds", SwitchDataThresholdsAction.class,
				new CommandRedirect("redirect", "main", 302)
		);
	}
}
