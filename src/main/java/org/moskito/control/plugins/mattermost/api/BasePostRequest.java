package org.moskito.control.plugins.mattermost.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

/**
 * Base class for POST requests.
 * {@link BaseRequest#getHttpRequestObject(String)} s defined
 * to return {@link HttpPost} object with json content type header
 * and json body, formed from request object map.
 * Also contains constructor corresponding to parent class.
 *
 * See parent class description for more details.
 *
 * @param <T> response object class
 */
public abstract class BasePostRequest <T extends BaseResponse> extends BaseRequest<T> {

    /**
     * Constructor matches parent constructor
     * See: {@link BaseRequest#BaseRequest(Class, MattermostApi)}
     *
     * @param clazz class of response object. Same as in generic template. Needed to instantiate new response object
     * @param api Mattermost API wrapper main class object. Needed to get authorization credentials
     */
    protected BasePostRequest(Class<T> clazz, MattermostApi api) {
        super(clazz, api);
    }

    /**
     * Method must return json body of request.
     * By default parses object fields map to json.
     * @return json body of request
     * @throws JsonProcessingException thrown by Jackson
     */
    /* test scope */ String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(getObjectMap());
    }

    /**
     * Method overwritten to return {@link HttpPost} object, filling it with
     * json body, made from object fields map.
     * @param url Mattermost API method url. used in return object constructor
     * @return {@link HttpPost} object to make POST requests
     */
    protected HttpRequestBase getHttpRequestObject(String url) {
        HttpPost httpRequest = new HttpPost(url);
        httpRequest.setHeader("Content-Type", "application/json");
        try {
            httpRequest.setEntity(new StringEntity(toJson()));
        } catch (UnsupportedEncodingException | JsonProcessingException ignored) {} // Means there is bug in API wrapper
        return httpRequest;
    }

}
