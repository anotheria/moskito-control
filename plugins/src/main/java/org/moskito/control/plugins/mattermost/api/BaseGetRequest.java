package org.moskito.control.plugins.mattermost.api;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * Base class for GET requests
 * Abstract method {@link BaseRequest#getHttpRequestObject(String)}
 * is defined to return {@link HttpGet} request object.
 * Also contains constructor corresponding to parent class.
 *
 * See parent class description for more details.
 *
 * @param <T> response object class
 */
public abstract class BaseGetRequest<T extends BaseResponse> extends BaseRequest<T>{

    /**
     * Constructor matches parent constructor
     * See: {@link BaseRequest#BaseRequest(Class, MattermostApi)}
     *
     * @param clazz class of response object. Same as in generic template. Needed to instantiate new response object
     * @param api Mattermost API wrapper main class object. Needed to get authorization credentials
     */
    protected BaseGetRequest(Class<T> clazz, MattermostApi api) {
        super(clazz, api);
    }

    /**
     * Method overwritten to return {@link HttpGet} object
     * @param url Mattermost API method url. used in return object constructor
     * @return {@link HttpGet} object to make GET requests
     */
    @Override
    protected HttpRequestBase getHttpRequestObject(String url){
        return new HttpGet(url);
    }

}
