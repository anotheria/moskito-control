package org.moskito.control.ui.action.inspection;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import org.apache.commons.lang3.StringUtils;
import org.moskito.control.common.AccumulatorDataItem;
import org.moskito.control.connectors.ConnectorException;
import org.moskito.control.connectors.response.ConnectorAccumulatorResponse;
import org.moskito.control.core.Component;
import org.moskito.control.core.ComponentRepository;
import org.moskito.control.core.chart.Chart;
import org.moskito.control.core.inspection.ComponentInspectionDataProvider;
import org.moskito.control.ui.action.BaseMoSKitoControlAction;
import org.moskito.control.ui.action.MainViewAction;

import java.util.*;

/**
 * Action for ajax-call to show accumulators charts for selected accumulators of component.
 *
 * @author Vladyslav Bezuhlyi
 */
public class ShowAccumulatorsChartsAction extends BaseMoSKitoControlAction {

    @Override
    public ActionCommand execute(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {
        String componentName = req.getParameter("componentName");
        ArrayList<String> accumulatorsNames = new ArrayList(Arrays.asList(req.getParameterValues("accumulators[]")));

        ConnectorAccumulatorResponse response = new ConnectorAccumulatorResponse();


        if (StringUtils.isEmpty(componentName)) {
            return mapping.error();
        }

        Component component = ComponentRepository.getInstance().getComponent(componentName);

        try {
            ComponentInspectionDataProvider provider = new ComponentInspectionDataProvider();
            response = provider.provideAccumulatorsCharts(component, accumulatorsNames);
        }
        catch (ConnectorException | IllegalStateException ex) {
            return mapping.error();
        }

        LinkedList<Chart> charts = new LinkedList<Chart>();
        Collection<String> names = response.getNames();
        for (String name : names) {
            List<AccumulatorDataItem> line = response.getLine(name);
            String accumulatorName = name+"-"+component.getName(); // to avoid same accumulators ids for multiple components
            Chart chart = new Chart(accumulatorName, -1);
            chart.addLine(component.getName(), accumulatorName);
            chart.notifyNewData(component.getName(), accumulatorName, line);
            charts.add(chart);
        }

        Collections.sort(charts, new Comparator<Chart>() {
            @Override
            public int compare(Chart chart, Chart another) {
                return chart.getName().compareTo(another.getName());
            }
        });
        req.setAttribute("chartBeans", MainViewAction.prepareChartData(charts));
        return mapping.success();
    }

}
