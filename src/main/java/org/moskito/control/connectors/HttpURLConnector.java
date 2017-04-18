package org.moskito.control.connectors;

import net.anotheria.util.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.moskito.control.connectors.httputils.HttpHelper;
import org.moskito.control.connectors.response.ConnectorAccumulatorResponse;
import org.moskito.control.connectors.response.ConnectorAccumulatorsNamesResponse;
import org.moskito.control.connectors.response.ConnectorStatusResponse;
import org.moskito.control.connectors.response.ConnectorThresholdsResponse;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.status.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Generic Http URL connector.
 *
 * @author dzhmud
 * @since 17.04.2017 1:31 PM
 */
public class HttpURLConnector implements Connector {

    /**
     * Target applications url.
     */
    private String location;

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(HttpURLConnector.class);

    @Override
    public void configure(String location, String credentials) {
        this.location = location;
    }

    @Override
    public ConnectorStatusResponse getNewStatus() {
        if (StringUtils.isEmpty(location)) {
            log.error("Location is absent!!");
            return new ConnectorStatusResponse(new Status(HealthColor.PURPLE, "Location is missing!"));
        }
        log.debug("URL to Call "+location);
        Status status;
        try {
            HttpResponse response = HttpHelper.getHttpResponse(location);
            String content = HttpHelper.getResponseContent(response);
            if (HttpHelper.isScOk(response)) {
                status = getStatus(content);
            } else {
                if (response != null && response.getStatusLine() != null) {
                    StatusLine line = response.getStatusLine();
                    String message = "StatusCode:"+line.getStatusCode()+", reason: " + line.getReasonPhrase();
                    status = new Status(HealthColor.RED, message);
                } else {
                    log.warn("Failed to connect to URL: " + location);
                    status = new Status(HealthColor.PURPLE, content);
                }
            }
        } catch (Throwable e) {
            log.warn("Failed to connect to URL: " + location, e);
            status = new Status(HealthColor.PURPLE, e.getMessage());
        }
        return new ConnectorStatusResponse(status);
    }


    private Status getStatus(String content) {
        final HealthColor result;
        content = (content==null) ? "" : content.trim();
        if ("DOWN".equalsIgnoreCase(content) || "FAILED".equalsIgnoreCase(content) ||
                "RED".equalsIgnoreCase(content)) {
            result = HealthColor.RED;
        } else if ("YELLOW".equalsIgnoreCase(content)) {
            result = HealthColor.YELLOW;
        } else {
            result = HealthColor.GREEN;
        }
        return new Status(result, content);
    }

    @Override
    public ConnectorThresholdsResponse getThresholds() {
        return null;
    }

    @Override
    public ConnectorAccumulatorResponse getAccumulators(List<String> accumulatorNames) {
        return null;
    }

    @Override
    public ConnectorAccumulatorsNamesResponse getAccumulatorsNames() throws IOException {
        return null;
    }
}
