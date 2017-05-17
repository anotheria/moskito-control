package org.moskito.control.plugins.mattermost.api.exceptions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Exception that indicates error in Mattermost API.
 * Means that data in request is invalid.
 *
 * See for more details:
 *  https://api.mattermost.com/#tag/errors
 */
@JsonIgnoreProperties(ignoreUnknown = true) // Mattermost API documentation seems not covers all response fields
public class MattermostAPIException extends Exception {

    /**
     * Description of error, returned by Mattermost API
     */
    @JsonProperty(value = "message")
    private String mattermostAPIMessage;
    /**
     * Mattermost API id of error
     */
    private String id;
    /**
     * Http status code of error
     */
    @JsonProperty(value = "status_code")
    private int statusCode;
    /**
     * Whether the error is OAuth specific
     */
    @JsonProperty(value = "is_oauth")
    private boolean isOauth;

    /**
     * Creates exception from error HTTP response
     * by parsing it from json body.
     * @param errorResponse http response with error
     * @return Mattermost API exception ready to be thrown
     * @throws IOException thrown by Apache or Jackson
     */
    public static MattermostAPIException parseException(HttpResponse errorResponse) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(
                mapper.getSerializationConfig()
                        .getDefaultVisibilityChecker()
                        .with(JsonAutoDetect.Visibility.ANY)
        );
        return mapper.readerFor(MattermostAPIException.class)
                .readValue(new InputStreamReader(errorResponse.getEntity().getContent()));
    }

    private MattermostAPIException(){
        super();
    }

    public String getMattermostAPIMessage() {
        return mattermostAPIMessage;
    }

    public String getId() {
        return id;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public boolean isOauth() {
        return isOauth;
    }

    @Override
    public String getMessage(){
        return "Mattermost API returned error. HTTP code : " + getStatusCode() +
                ", error id : " + getId() + ", message : " + getMattermostAPIMessage();
    }

}
