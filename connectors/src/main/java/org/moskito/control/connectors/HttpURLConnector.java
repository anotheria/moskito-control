package org.moskito.control.connectors;

import net.anotheria.moskito.core.accumulation.AccumulatedValue;
import net.anotheria.moskito.core.accumulation.Accumulator;
import net.anotheria.moskito.core.accumulation.AccumulatorRepository;
import net.anotheria.moskito.core.accumulation.Accumulators;
import net.anotheria.moskito.core.config.MoskitoConfigurationHolder;
import net.anotheria.moskito.core.config.dashboards.DashboardConfig;
import net.anotheria.moskito.core.config.dashboards.DashboardsConfig;
import net.anotheria.moskito.core.dynamic.OnDemandStatsProducer;
import net.anotheria.moskito.core.dynamic.OnDemandStatsProducerException;
import net.anotheria.moskito.core.predefined.ServiceStatsFactory;
import net.anotheria.moskito.core.producers.CallExecution;
import net.anotheria.moskito.core.producers.IStatsProducer;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;
import net.anotheria.moskito.core.threshold.*;
import net.anotheria.moskito.core.threshold.guard.DoubleBarrierPassGuard;
import net.anotheria.moskito.core.threshold.guard.GuardedDirection;
import net.anotheria.util.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.StatusLine;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.moskito.control.common.AccumulatorDataItem;
import org.moskito.control.common.HealthColor;
import org.moskito.control.common.Status;
import org.moskito.control.common.ThresholdDataItem;
import org.moskito.control.config.ComponentConfig;
import org.moskito.control.config.HeaderParameter;
import org.moskito.control.config.HttpMethodType;
import org.moskito.control.connectors.httputils.HttpHelper;
import org.moskito.control.connectors.parsers.ParserHelper;
import org.moskito.control.connectors.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic Http URL connector.
 *
 * @author dzhmud
 * @since 17.04.2017 1:31 PM
 */
public class HttpURLConnector extends AbstractConnector {

    /**
     * Target applications url.
     */
    private String location;

    /**
     * Component name.
     */
    private String componentName;

    /**
     * Target URL credentials.
     */
    private UsernamePasswordCredentials credentials;

    /**
     * Request method type.
     */
    private HttpMethodType methodType;

    /**
     * Request payload if payload-friendly method was provided.
     */
    private String payload;

    /**
     * Content-Type of payload.
     */
    private String contentType;

    /**
     * Request headers.
     */
    private Header[] headers;

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(HttpURLConnector.class);
    private static Header gzipHeader = new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip");

    @Override
    public void configure(ComponentConfig config) {
        this.componentName = config.getName();
        this.location = config.getLocation();
        this.credentials = ParserHelper.getCredentials(config.getCredentials());

        String method = config.getData().get("methodType");
        this.methodType = method == null ? null : HttpMethodType.valueOf(method);
        this.payload = config.getData().get("payload");
        this.contentType = config.getData().get("contentType");
        HeaderParameter[] headers = config.getHeaders();

        if (headers != null) {
            this.headers = new Header[headers.length + 1];
            for (int i = 0; i < headers.length; i++) {
                this.headers[i] = new BasicHeader(headers[i].getKey(), headers[i].getValue());
            }

            this.headers[this.headers.length - 1] = gzipHeader;
        }

        IStatsProducer producer = ProducerRegistryFactory.getProducerRegistryInstance().getProducer(componentName + "-Producer");
        if (producer == null) {
            initProducer();
        }
    }

    private void initProducer() {
        ProducerRegistryFactory.getProducerRegistryInstance().registerProducer(new OnDemandStatsProducer(componentName + "-Producer", "use-case", "GET", ServiceStatsFactory.DEFAULT_INSTANCE));
        Accumulators.createAccumulator(componentName + "-AVG.1m", componentName + "-Producer", "GET", "Avg", "1m");
        Accumulators.createAccumulator(componentName + "-AVG.15m", componentName + "-Producer", "GET", "Avg", "15m");
        Accumulators.createAccumulator(componentName + "-AVG.1h", componentName + "-Producer", "GET", "Avg", "1h");
        ThresholdConditionGuard[] guards = new ThresholdConditionGuard[]{
                new DoubleBarrierPassGuard(ThresholdStatus.GREEN, 1000, GuardedDirection.DOWN),
                new DoubleBarrierPassGuard(ThresholdStatus.YELLOW, 1000, GuardedDirection.UP),
                new DoubleBarrierPassGuard(ThresholdStatus.ORANGE, 2000, GuardedDirection.UP),
                new DoubleBarrierPassGuard(ThresholdStatus.RED, 5000, GuardedDirection.UP),
                new DoubleBarrierPassGuard(ThresholdStatus.PURPLE, 20000, GuardedDirection.UP)
        };
        Thresholds.addThreshold(componentName + "-AVG.1m", componentName + "-Producer", "GET", "Avg", "1m", guards);

        DashboardsConfig dashboardsConfig = MoskitoConfigurationHolder.getConfiguration().getDashboardsConfig();
        if (dashboardsConfig == null) {
            dashboardsConfig = new DashboardsConfig();
        }
        if (dashboardsConfig.getDashboards() == null) {
            dashboardsConfig.setDashboards(new DashboardConfig[]{});
        }

        MoskitoConfigurationHolder.getConfiguration().setDashboardsConfig(dashboardsConfig);
    }

    @Override
    public ConnectorStatusResponse getNewStatus() {
        if (StringUtils.isEmpty(location)) {
            log.error("Location is absent!!");
            return new ConnectorStatusResponse(new Status(HealthColor.PURPLE, "Location is missing!"));
        }
        log.debug("URL to Call " + location);
        CallExecution execution = null;
        try {
            OnDemandStatsProducer producer = (OnDemandStatsProducer) ProducerRegistryFactory.getProducerRegistryInstance().getProducer(componentName + "-Producer");
            if (producer != null) {
                execution = producer.getStats("GET").createCallExecution();
                execution.startExecution(componentName + "-AVG");
            }
        } catch (OnDemandStatsProducerException e) {
            log.warn("Couldn't count this call due to producer error", e);
        }
        Status status;
        try {
            ContentType parsed = contentType == null ? null : ContentType.parse(contentType);
            CloseableHttpResponse response = HttpHelper.getHttpResponse(location, credentials, headers, methodType, payload, parsed);
            String content = HttpHelper.getResponseContent(response);
            if (HttpHelper.isScOk(response)) {
                status = getStatus(content);
            } else {
                if (response.getStatusLine() != null) {
                    StatusLine line = response.getStatusLine();
                    String message = "StatusCode:" + line.getStatusCode() + ", reason: " + line.getReasonPhrase();
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
        ConnectorStatusResponse newStatus = new ConnectorStatusResponse(status);
        if (execution != null)
            execution.finishExecution();
        return newStatus;
    }


    private Status getStatus(String content) {
        HealthColor result;
        content = (content == null) ? "" : content.trim();
        switch (content.toLowerCase()) {
            case "down":
            case "red":
            case "failed":
                result = HealthColor.RED;
                break;
            case "yellow":
                result = HealthColor.YELLOW;
                break;
            default:
                result = HealthColor.GREEN;
                content = "";
        }
        ConnectorThresholdsResponse thresholds = getThresholds();
        if (!thresholds.getItems().isEmpty()) {
            ThresholdDataItem dataItem = thresholds.getItems().get(thresholds.getItems().size() - 1);
            if (!dataItem.getStatus().equals(HealthColor.NONE)) {
                result = dataItem.getStatus();
                content = getStatusMessage(result, dataItem.getName());
            }
        }
        return new Status(result, content);
    }

    private String getStatusMessage(HealthColor color, String threshold) {
        switch (color) {
            case GREEN:
                return threshold + ": < 1000";
            case YELLOW:
                return threshold + ": [1000 TO 2000]";
            case ORANGE:
                return threshold + ": [2000 TO 5000]";
            case RED:
                return threshold + ": [5000 TO 20000]";
            case PURPLE:
                return threshold + ": > 20000";
            default:
                return "";
        }
    }

    @Override
    public ConnectorThresholdsResponse getThresholds() {
        ConnectorThresholdsResponse response = new ConnectorThresholdsResponse();
        List<ThresholdDataItem> dataItems = new ArrayList<>();
        for (Threshold threshold : ThresholdRepository.getInstance().getThresholds()) {
            if (threshold.getName().startsWith(componentName + "-AVG")) {
                ThresholdDataItem dataItem = new ThresholdDataItem();
                dataItem.setName(threshold.getName());
                dataItem.setStatus(HealthColor.getHealthColor(threshold.getStatus()));
                dataItem.setLastValue(threshold.getLastValue());
                dataItem.setStatusChangeTimestamp(threshold.getStatusChangeTimestamp());
                dataItems.add(dataItem);
            }
        }
        response.setItems(dataItems);
        return response;
    }

    @Override
    public ConnectorAccumulatorResponse getAccumulators(List<String> accumulatorNames) {
        ConnectorAccumulatorResponse response = new ConnectorAccumulatorResponse();
        for (Accumulator accumulator : AccumulatorRepository.getInstance().getAccumulators()) {
            String accName = accumulator.getName();
            if (accName.startsWith(componentName + "-AVG")) {
                accName = accName.substring(componentName.length() + 1);
            }

            if (accumulatorNames.contains(accName)) {
                List<AccumulatorDataItem> dataItems = new ArrayList<>();
                for (AccumulatedValue accumulatedValue : accumulator.getValues()) {
                    dataItems.add(new AccumulatorDataItem(accumulatedValue.getTimestamp(), accumulatedValue.getValue()));
                }
                response.addDataLine(accName, dataItems);
            }
        }
        return response;
    }

    @Override
    public ConnectorAccumulatorsNamesResponse getAccumulatorsNames() throws IOException {
        List<String> names = new ArrayList<>();
        for (Accumulator accumulator : AccumulatorRepository.getInstance().getAccumulators()) {
            if (accumulator.getName().startsWith(componentName + "-AVG")) {
                names.add(accumulator.getName().substring(componentName.length() + 1));
            }
        }
        return new ConnectorAccumulatorsNamesResponse(names);
    }

    @Override
    public ConnectorInformationResponse getInfo() {
        return null;
    }

    @Override
    public boolean supportsThresholds() {
        return true;
    }

    @Override
    public boolean supportsAccumulators() {
        return true;
    }
}
