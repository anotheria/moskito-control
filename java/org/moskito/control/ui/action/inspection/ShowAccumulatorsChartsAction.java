package org.moskito.control.ui.action.inspection;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import org.moskito.control.ui.action.BaseMoSKitoControlAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Vladyslav Bezuhlyi
 */
public class ShowAccumulatorsChartsAction extends BaseMoSKitoControlAction {

    @Override
    public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) throws Exception {
        ArrayList<String> accumulators = new ArrayList(Arrays.asList(req.getParameterValues("accumulators[]")));
        req.setAttribute("accumulators", accumulators);
        return mapping.success();
    }

}
