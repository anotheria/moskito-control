package org.moskito.control.ui;

import net.anotheria.maf.action.ActionMappings;
import net.anotheria.maf.action.ActionMappingsConfigurator;
import net.anotheria.maf.action.CommandForward;
import net.anotheria.maf.action.CommandRedirect;
import org.moskito.control.ui.action.AddStatusFilterAction;
import org.moskito.control.ui.action.ClearCategoryFilterAction;
import org.moskito.control.ui.action.ClearStatusFilterAction;
import org.moskito.control.ui.action.MainViewAction;
import org.moskito.control.ui.action.MuteNotificationsAction;
import org.moskito.control.ui.action.RemoveStatusFilterAction;
import org.moskito.control.ui.action.SelectViewAction;
import org.moskito.control.ui.action.SelectCategoryAction;
import org.moskito.control.ui.action.ShowConfigurationAction;
import org.moskito.control.ui.action.ShowDataRepositoryAction;
import org.moskito.control.ui.action.SwitchChartsAction;
import org.moskito.control.ui.action.SwitchHistoryAction;
import org.moskito.control.ui.action.SwitchStatusAction;
import org.moskito.control.ui.action.SwitchTvAction;
import org.moskito.control.ui.action.TriggerTestAlertAction;
import org.moskito.control.ui.action.UnmuteNotificationsAction;
import org.moskito.control.ui.action.inspection.ExecuteComponentActionCommandAction;
import org.moskito.control.ui.action.inspection.ShowAccumulatorsChartsAction;
import org.moskito.control.ui.action.inspection.ShowAccumulatorsListAction;
import org.moskito.control.ui.action.inspection.ShowComponentActionsListAction;
import org.moskito.control.ui.action.inspection.ShowComponentConfigAction;
import org.moskito.control.ui.action.inspection.ShowComponentInformationAction;
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
				new CommandForward("success", "/org/moskito/control/ui/jsp/MainView.jsp")
		);

		actionMappings.addMapping("setView", SelectViewAction.class,
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
                new CommandForward("success", "/org/moskito/control/ui/jsp/inspection/Thresholds.jsp"),
                new CommandForward("error", "/org/moskito/control/ui/jsp/inspection/NoDataAvailable.jsp")
        );
        actionMappings.addMapping("accumulatorsList", ShowAccumulatorsListAction.class,
                new CommandForward("success", "/org/moskito/control/ui/jsp/inspection/AccumulatorsList.jsp"),
                new CommandForward("error", "/org/moskito/control/ui/jsp/inspection/NoDataAvailable.jsp")
        );
        actionMappings.addMapping("accumulatorsCharts", ShowAccumulatorsChartsAction.class,
                new CommandForward("success", "/org/moskito/control/ui/jsp/inspection/AccumulatorsCharts.jsp"),
                new CommandForward("error", "/org/moskito/control/ui/jsp/inspection/NoDataAvailable.jsp")
        );
		actionMappings.addMapping("connectorInformation", ShowConnectorInformationAction.class,
				new CommandForward("success", "/org/moskito/control/ui/jsp/inspection/ConnectorInformation.jsp"),
				new CommandForward("error", "/org/moskito/control/ui/jsp/inspection/NoDataAvailable.jsp")
		);
		actionMappings.addMapping("componentInformation", ShowComponentInformationAction.class,
				new CommandForward("success", "/org/moskito/control/ui/jsp/inspection/ConnectorInformation.jsp"),
				new CommandForward("error", "/org/moskito/control/ui/jsp/inspection/NoDataAvailable.jsp")
		);
		actionMappings.addMapping("componentHistory", ShowHistoryAction.class,
				new CommandForward("success", "/org/moskito/control/ui/jsp/inspection/History.jsp"),
				new CommandForward("error", "/org/moskito/control/ui/jsp/inspection/NoDataAvailable.jsp")
		);
		actionMappings.addMapping("componentConfig", ShowComponentConfigAction.class,
				new CommandForward("success", "/org/moskito/control/ui/jsp/inspection/ComponentConfig.jsp"),
				new CommandForward("error", "/org/moskito/control/ui/jsp/inspection/NoDataAvailable.jsp")
		);
        actionMappings.addMapping("componentActionInformation", ShowComponentActionsListAction.class,
                new CommandForward("success", "/org/moskito/control/ui/jsp/inspection/ComponentActionInformation.jsp"),
                new CommandForward("error", "/org/moskito/control/ui/jsp/inspection/NoDataAvailable.jsp")
        );
        actionMappings.addMapping("executeComponentActionCommand", ExecuteComponentActionCommandAction.class,
                new CommandForward("success", "/org/moskito/control/ui/jsp/inspection/CommandResult.jsp"),
                new CommandForward("error", "/org/moskito/control/ui/jsp/inspection/NoDataAvailable.jsp")
        );

		//data repository
		actionMappings.addMapping("dataRepository", ShowDataRepositoryAction.class,
				new CommandRedirect("redirect", "main", 302)
		);

		actionMappings.addMapping("triggerTestAlert", TriggerTestAlertAction.class);
	}
}
