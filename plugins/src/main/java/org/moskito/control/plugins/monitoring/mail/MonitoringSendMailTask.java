package org.moskito.control.plugins.monitoring.mail;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import net.anotheria.util.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.util.Base64;

/**
 * Trigger external api to send mail.
 *
 * @author ynikonchuk
 */
public class MonitoringSendMailTask {

    private static final Logger log = LoggerFactory.getLogger(MonitoringSendMailTask.class);

    /**
     * Config.
     */
    private final MonitoringMailConfig config;

    private final Client client;

    public MonitoringSendMailTask(MonitoringMailConfig config) {
        this.config = config;
        this.client = Client.create();
        this.client.setConnectTimeout(5000);
        this.client.setReadTimeout(10000);
    }

    /**
     * Executes task.
     */
    public Result execute() {
        MonitoringMailSendEndpointConfig sendConfig = config.getSendMailConfig();
        if (sendConfig == null) {
            log.debug("no sendEndpointConfig for: " + config.getName());
            return new Result(false, config);
        }

        log.info("execute(). config: " + sendConfig.getApiEndpoint());
        boolean success = executeSendMail(sendConfig);
        return new Result(success, config);
    }

    private boolean executeSendMail(MonitoringMailSendEndpointConfig config) {
        try {

            WebResource resource = client.resource(config.getApiEndpoint());

            // set auth if needed
            setBasicAuthHeader(resource, config);
            setAuthHeader(resource, config);

            ClientResponse response = resource
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .post(ClientResponse.class, new SendMailRequest(config.getEmail()));

            if (log.isDebugEnabled()) {
                log.debug("status: " + response.getStatus());
            }

            boolean successResponse = response.getStatus() == HttpStatus.SC_OK;
            if (!successResponse) {
                log.error("failed to send mail. response: {}", response.getEntity(String.class));
            }
            return successResponse;
        } catch (Exception e) {
            log.error("executeSendMail(). endpoint: {}, failed: {}", config.getApiEndpoint(), e.getMessage(), e);
            return false;
        }
    }

    private static class SendMailRequest {

        private String email;

        public SendMailRequest() {
        }

        public SendMailRequest(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class Result {
        private final boolean success;
        private final MonitoringMailConfig config;

        public Result(boolean success, MonitoringMailConfig config) {
            this.success = success;
            this.config = config;
        }

        public boolean isSuccess() {
            return success;
        }

        public MonitoringMailConfig getConfig() {
            return config;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "success=" + success +
                    ", config=" + config +
                    '}';
        }
    }


    private void setBasicAuthHeader(WebResource resource, MonitoringMailSendEndpointConfig config) {
        if (StringUtils.isEmpty(config.getBasicAuthName())) {
            return;
        }

        String basicAuthVal = getBasicAuthVal(config);
        resource.header("Authorization", "Basic " + basicAuthVal);
    }

    private void setAuthHeader(WebResource resource, MonitoringMailSendEndpointConfig config) {
        if (StringUtils.isEmpty(config.getAuthHeaderName())) {
            return;
        }

        resource.header(config.getAuthHeaderName(), config.getAuthHeaderValue());
    }

    private String getBasicAuthVal(MonitoringMailSendEndpointConfig config) {
        String authString = config.getBasicAuthName() + ":" + config.getBasicAuthPass();
        return Base64.getEncoder().encodeToString(authString.getBytes());
    }

}
