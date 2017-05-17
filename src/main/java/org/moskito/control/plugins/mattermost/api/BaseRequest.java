package org.moskito.control.plugins.mattermost.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.moskito.control.plugins.mattermost.api.annotations.FieldName;
import org.moskito.control.plugins.mattermost.api.annotations.IgnoreField;
import org.moskito.control.plugins.mattermost.api.exceptions.MattermostAPIException;
import org.moskito.control.plugins.mattermost.api.exceptions.MattermostAPIInternalException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic class for requests to Mattermost API.
 *
 * Generic parameter of this class (and class parameter in constructor)
 * determinate response object class corresponding to request.
 *
 * Class and it`s child uses Apache http components library and Jackson library.
 * To make http requests and encode or decode objects with request and response data to or from json.
 *
 * Request invoked by calling {@link BaseRequest#makeRequest(String)}
 * method with string url template parameter:
 *  First {@link BaseRequest#buildUrl(String)} method are called.
 *  This method invokes building map from request object fields. (see {@link BaseRequest#convertObjectToMap(Object)})
 *  Keys names formed from field names changing camelCase name style to underscore (as Mattermost API require)
 *  Some fields may be ignored or renamed using annotations ({@link IgnoreField}, {@link FieldName})
 *  Then url template filled from object map.
 *  Second abstract method {@link BaseRequest#getHttpRequestObject(String)} is called
 *  witch returns org.apache.http.client.methods.HttpRequestBase class object from Apache library.
 *  In post requests this method also parses object map to json and uses it to fill request body.
 *  This object is used to make a request.
 *  Then response object from generic parameter is returned
 *  by calling {@link BaseRequest#createResponseObject(HttpResponse)} method.
 *  Method populates response object from response json.
 *
 * Due class contains self-parsing mechanism and all necessary for making request
 * in most situations only fields (and proper setters and getters)
 * are declared in child final classes.
 *
 * @param <T> response class
 */
public abstract class BaseRequest <T extends BaseResponse> {

    /**
     * Class of response object, must be same as generic parameter
     * Required for calling constructor.
     */
    private Class<T> clazz;
    /**
     * Main API wrapper class
     * Token for request taken from here
     */
    private MattermostApi api;
    /**
     * Parsed request fields.
     * Used for forming url and json request body
     * See class description for more details
     */
    private Map<String, Object> objectMap;

    /**
     * Wrapper method for getting request object map.
     * Creates map on first call.
     * Used for forming url and json request body
     * @return map with parsed request object fields
     */
    protected Map<String, Object> getObjectMap(){

        if(objectMap == null)
            objectMap = convertObjectToMap(this);

        return objectMap;

    }

    /**
     * Returns MattermostAPI object.
     * Used in child classes
     * @return MattermostAPI class instance
     */
    protected MattermostApi getApi() {
        return api;
    }

    /**
     * First parameter defines class of response object
     * must be same as in generic template.
     * Second MattermostAPI class instance required for authorized calls to API
     *
     * @param clazz class of response object
     * @param api MattermostAPI instance
     */
    BaseRequest(Class<T> clazz, MattermostApi api)
    {
        this.clazz = clazz;
        this.api = api;
    }

    /**
     * Creates response object from http response.
     * Returns new object of class specified in generic parameter and constructor
     * populated from response json.
     * @param response http response of Mattermost API
     *
     * @return populated from response json response object
     * @throws InstantiationException should not be thrown, if there is no bugs in API wrapper module
     * @throws IllegalAccessException should not be thrown, if there is no bugs in API wrapper module
     * @throws IOException thrown by Apache http library or Jackson
     * @throws com.fasterxml.jackson.core.JsonProcessingException thrown by Jackson
     */
    /* test scope */ T createResponseObject(HttpResponse response)
            throws InstantiationException, IllegalAccessException, IOException {
        T responseObject = clazz.newInstance();
        responseObject.populateResponse(response);
        return responseObject;
    }

    /**
     * Used to convert camelCase fields names to underscore as Mattermost API required
     * @param camelCaseString string to convert
     * @return underscore style string
     */
    private String camelCaseToUnderscore(String camelCaseString){
        return camelCaseString.replaceAll("([A-Z]+)","\\_$1").toLowerCase();
    }

    /**
     * Parses object fields to map.
     * Uses field names as keys and field values as values.
     * Field names are converted from camelCase to underscore.
     * Field must be public or contain
     * public getter with field name (private String teamName; => public String getTeamName(){...})
     * Also field can be ignored or renamed using annotations (see class description for more details)
     * @param obj object to parse map from
     * @return map with parsed object fields
     */
    /* Test Scope */ Map<String, Object> convertObjectToMap(Object obj) {

        Map<String, Object> fieldsMap = new HashMap<>();

        Class clazz = obj.getClass();

        for (Field field : clazz.getDeclaredFields()){

            // Skipping field with ignore annotation
            if(field.getAnnotation(IgnoreField.class) == null){

                // Name, that be written to map
                String fieldName;
                // Real field name. Needed for finding getter
                String realFieldName = field.getName();
                // Field value
                Object fieldValue;

                // Getting field name from annotation, if it present
                if(field.getAnnotation(FieldName.class) != null){
                    fieldName = field.getAnnotation(FieldName.class).name();
                }
                // Getting field name by converting it camelCase name to underscore (means field name annotation not present)
                else
                    fieldName = camelCaseToUnderscore(realFieldName);

                // If field is public, value taken directly from it
                if((field.getModifiers() & Modifier.PUBLIC) != 0){
                    try {
                        fieldValue = field.get(obj);
                    } catch (IllegalAccessException ignored) {continue;} // Field access is checked
                }
                // Else trying to find and invoke getter
                else {

                    try {
                        Method getter = clazz.getMethod("get" + StringUtils.capitalize(realFieldName));
                        fieldValue = getter.invoke(obj);
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        continue; // Filed to find or invoke getter. Slipping this field
                    }

                }

                // Checking is field value not null
                if(fieldValue != null && !fieldValue.equals(0L))
                    // Field name and value are successfully found.
                    // Putting it to map
                    fieldsMap.put(fieldName, fieldValue);

            }

        }

        return fieldsMap;

    }

    /**
     * Adds "$" sign to beginning of string.
     * Such format names are used in url templates
     * @param fieldName name of field to convert into template variable
     * @return template variable string
     */
    private String convertFieldNameToTemplate(String fieldName){
        return "$" + fieldName;
    }

    /**
     * Resolves url template filling it variables from object map.
     * Template variables starts with "$" sign and ends with request class field name,
     * from where value is taken.
     * Example:
     *  "teams/$team_id/channels/name/$channel_name" => "teams/5da4a5ad54544554ad/channels/name/moskito-alerts"
     * @param urlTemplate template of url
     * @return builded url ready to make request
     */
    /* Test Scope */ String buildUrl(String urlTemplate){

        Map<String, Object> requestFields = getObjectMap();
        String url = urlTemplate;

        for (Map.Entry<String, Object> field : requestFields.entrySet()){

            url = StringUtils.replace(
                    url,
                    convertFieldNameToTemplate(field.getKey()),
                    // Null check prevents null pointer exception in case object field not initialized
                    field.getValue() != null ? field.getValue().toString() : ""
            );

        }

        return url;

    }

    /**
     * Triggers making request to Mattermost API.
     * Builds url and body (in case of post requests) of request and
     * makes request to Mattermost API.
     *
     * @param url Mattermost API method url template
     * @return response object corresponding to request
     */
     public T makeRequest(String url) throws IOException, MattermostAPIException, MattermostAPIInternalException {

        HttpClient client = new DefaultHttpClient();

        HttpRequestBase httpRequest = getHttpRequestObject(buildUrl(url));

        if(api.isAuthorized())
            httpRequest.setHeader("Authorization", "Bearer " + api.getToken());

        try {

            HttpResponse httpResponse = client.execute(httpRequest);

            if(httpResponse.getStatusLine().getStatusCode() != 200)
                // Means there is an errors in API request data (Non API wrapper bug)
                throw MattermostAPIException.parseException(httpResponse);

            return createResponseObject(httpResponse);

        } catch (IllegalAccessException | InstantiationException | JsonProcessingException e) {
            throw new MattermostAPIInternalException("Failed to make request due Mattermost API wrapper bugs", e);
        }

    }

    protected abstract HttpRequestBase getHttpRequestObject(String url);

}
