package org.moskito.control.ui.action.inspection;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import org.apache.commons.lang.StringUtils;
import org.moskito.control.connectors.response.ConnectorAccumulatorResponse;
import org.moskito.control.core.Application;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.Component;
import org.moskito.control.core.accumulator.AccumulatorDataItem;
import org.moskito.control.core.chart.Chart;
import org.moskito.control.core.inspection.ComponentInspectionDataProvider;
import org.moskito.control.ui.action.BaseMoSKitoControlAction;
import org.moskito.control.ui.action.MainViewAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Action for ajax-call to show accumulators charts for selected accumulators of component.
 *
 * @author Vladyslav Bezuhlyi
 */
public class ShowAccumulatorsChartsAction extends BaseMoSKitoControlAction {

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(ShowAccumulatorsChartsAction.class);


    @Override
    public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) throws Exception {
        String applicationName = (String) req.getSession().getAttribute(ATT_APPLICATION);
        String componentName = req.getParameter("componentName");
        ArrayList<String> accumulatorsNames = new ArrayList(Arrays.asList(req.getParameterValues("accumulators[]")));

        if (StringUtils.isEmpty(applicationName) || StringUtils.isEmpty(componentName)) {
            return mapping.error();
        }
        Application application = ApplicationRepository.getInstance().getApplication(applicationName);
        if (application == null) {
            return mapping.error();
        }
        Component component = application.getComponent(componentName);
        if (component == null) {
            return mapping.error();
        }

        ComponentInspectionDataProvider provider = new ComponentInspectionDataProvider();
        ConnectorAccumulatorResponse response = provider.provideAccumulatorsCharts(application, component, accumulatorsNames);

        LinkedList<Chart> charts = new LinkedList<Chart>();
        Collection<String> names = response.getNames();
        for (String name : names) {
            List<AccumulatorDataItem> line = response.getLine(name);
            String accumulatorName = name+"-"+application.getName()+"-"+component.getName(); // to avoid same accumulators ids for multiple components
            Chart chart = new Chart(application, accumulatorName, -1);
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
