package org.moskito.control.ui.action.inspection;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import net.anotheria.util.NumberUtils;
import net.anotheria.util.StringUtils;
import org.moskito.control.core.Component;
import org.moskito.control.core.ComponentRepository;
import org.moskito.control.ui.action.BaseMoSKitoControlAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Action for ajax-call to show connector information.
 *
 * @author strel
 */
public class ShowComponentInformationAction extends BaseMoSKitoControlAction {

    @Override
    public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) throws Exception {

        String componentName = req.getParameter("componentName");
        if (StringUtils.isEmpty(componentName)) {
            return mapping.error();
        }

        Component component = ComponentRepository.getInstance().getComponent(componentName);
		Map<String, String> info = new HashMap<>();

		info.put("Name", component.getName());
		info.put("Category", component.getCategory());
		info.put("Location", component.getConfiguration().getLocation());
		info.put("Connector type", component.getConfiguration().getConnectorType().name());
		info.put("Tags", ""+component.getTags());
		info.put("Last Update ts", ""+component.getLastUpdateTimestamp());
		info.put("Last Update", NumberUtils.makeISO8601TimestampString(component.getLastUpdateTimestamp()));
		info.put("Update age", ""+((System.currentTimeMillis() - component.getLastUpdateTimestamp())/1000)+" sec" );
		info.put("Update type", component.isDynamic() ? "push":"pull");


        req.setAttribute("connectorInformation", info);
        return mapping.success();
    }

}
