package org.moskito.control.ui.action.inspection;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import net.anotheria.util.NumberUtils;
import net.anotheria.util.StringUtils;
import org.moskito.control.config.ConnectorType;
import org.moskito.control.config.HeaderParameter;
import org.moskito.control.config.HttpMethodType;
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
    public ActionCommand execute(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {

        String componentName = req.getParameter("componentName");
        if (StringUtils.isEmpty(componentName)) {
            return mapping.error();
        }

        Component component = ComponentRepository.getInstance().getComponent(componentName);
        Map<String, String> info = new HashMap<>();

        info.put("Name", component.getName());
        info.put("Category", component.getCategory());
        //Component configuration is null for dynamic components.
        if (component.getConfiguration() != null) {
            info.put("Location", component.getConfiguration().getLocation());
            info.put("Connector type", component.getConfiguration().getConnectorType().name());
        }
        info.put("Tags", "" + component.getTags());
        info.put("Last Update ts", "" + component.getLastUpdateTimestamp());
        info.put("Last Update", NumberUtils.makeISO8601TimestampString(component.getLastUpdateTimestamp()));
        info.put("Update age", "" + ((System.currentTimeMillis() - component.getLastUpdateTimestamp()) / 1000) + " sec");
        info.put("Update type", component.isDynamic() ? "push" : "pull");

        if (component.getConfiguration().getConnectorType() == ConnectorType.URL) {
            String method = component.getConfiguration().getData().get("methodType");
            HttpMethodType methodType = method == null ? null : HttpMethodType.valueOf(method);
            if (methodType != null) {
                info.put("Method Type", methodType.name());
            }

            String payload = component.getConfiguration().getData().get("payload");
            if (payload != null) {
                info.put("Payload", formatPayload(payload));
            }

            String contentType = component.getConfiguration().getData().get("contentType");
            if (contentType != null) {
                info.put("Content-Type", contentType);
            }

            if (component.getConfiguration().getHeaders() != null) {
                info.put("Headers", formatHeaders(component.getConfiguration().getHeaders()));
            }
        }
        req.setAttribute("connectorInformation", info);

        Map<String, String> attributes = component.getAttributes();
        if (attributes != null && attributes.size() > 0)
            req.setAttribute("componentAttributes", attributes);

        return mapping.success();
    }

    private String formatPayload(String payload) {
        if (payload.length() > 100) {
            return payload.substring(0, 100) + "...";
        }
        return payload;
    }

    private String formatHeaders(HeaderParameter[] headers) {
        if (headers == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        int counter = 1;
        for (HeaderParameter header : headers) {
            result.append(header.getKey()).append("<b>:</b>");
            if (header.getValue().length() > 5) {
                result.append(header.getValue(), 0, 5).append("...");
            } else {
                result.append(header.getValue());
            }
            if (counter != headers.length) {
                result.append(";");
            }
            counter++;
        }
        return result.toString();
    }

}
