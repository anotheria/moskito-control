package org.moskito.control.plugins.monitoring.mail;

import org.apache.http.HttpStatus;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Base64;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

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
        this.client = new JerseyClientBuilder()
                .readTimeout(60_000, TimeUnit.MILLISECONDS)
                .connectTimeout(60_000, TimeUnit.MILLISECONDS)
                .build();
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

            WebTarget webTarget = client.target(config.getApiEndpoint());

            /*
            // set auth if needed
            setBasicAuthHeader(resource, config);
            setAuthHeader(resource, config);
            */

            Response response = null;
            Entity<String> entity = Entity.entity(new SendMailRequest(config.getEmail()).toString(), MediaType.APPLICATION_JSON_TYPE);
            response = webTarget.request().
                    accept(MediaType.APPLICATION_JSON).
                    post(entity);


            String content = response.readEntity(String.class);

            if (log.isDebugEnabled()) {
                log.debug("status: " + response.getStatus());
            }

            boolean successResponse = response.getStatus() == HttpStatus.SC_OK;
            if (!successResponse) {
                log.error("failed to send mail. response: {}", content);
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


    private void setBasicAuthHeader(WebTarget resource, MonitoringMailSendEndpointConfig config) {
        /* TODO MIGRATE
        if (StringUtils.isEmpty(config.getBasicAuthName())) {
            return;
        }

        String basicAuthVal = getBasicAuthVal(config);
        resource.header("Authorization", "Basic " + basicAuthVal);

         */
    }

    private void setAuthHeader(WebTarget resource, MonitoringMailSendEndpointConfig config) {
        /* TODO MIGRATE
        if (StringUtils.isEmpty(config.getAuthHeaderName())) {
            return;
        }

        resource.header(config.getAuthHeaderName(), config.getAuthHeaderValue());
        */

    }

    private String getBasicAuthVal(MonitoringMailSendEndpointConfig config) {
        String authString = config.getBasicAuthName() + ":" + config.getBasicAuthPass();
        return Base64.getEncoder().encodeToString(authString.getBytes());
    }

}
