package org.moskito.control.plugins.mattermost.api;

import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Test;
import org.moskito.control.plugins.mattermost.api.annotations.FieldName;
import org.moskito.control.plugins.mattermost.api.annotations.IgnoreField;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Tests some methods from BaseRequest class
 */
public class BaseRequestTest {

    /**
     * Provides expected map from {@link BaseRequestTest#testObjectToMapConverting()} test
     * @return expected map from test
     */
    private static Map<String, Object> getExpectedMapForObjectToMapConvertingTest(){

        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("camel_case_field", "camelCase");
        expectedMap.put("new_field_name", "renamedField");
        expectedMap.put("public_field", "public");

        return expectedMap;

    }

    /**
     * Tests parsing request object to map mechanism.
     */
    @Test public void testObjectToMapConverting(){

        RequestObjectMock requestMock = new RequestObjectMock();
        Map<String, Object> requestMap = requestMock.convertObjectToMap(requestMock);

        assertEquals(getExpectedMapForObjectToMapConvertingTest(), requestMap);

    }

    /**
     * Test building url from template
     */
    @Test public void testBuildUrl(){

        final String urlTemplate = "http://mattermost_host/somemothod/$camel_case_field/$new_field_name";
        final String expectedUrl = "http://mattermost_host/somemothod/camelCase/renamedField";

        RequestObjectMock requestMock = new RequestObjectMock();

        assertEquals(expectedUrl, requestMock.buildUrl(urlTemplate));

    }

    /**
     * Needed to be inserted in generic template of BaseRequest mock
     */
    public static class ResponseObjectMock extends BaseResponse{

    }

    /**
     * Extends abstract BaseRequest class
     */
    public static class RequestObjectMock extends BaseRequest<ResponseObjectMock>{

        @IgnoreField
        private String ignoredField; // Have to be ignored

        private String camelCaseField = "camelCase"; // Have to be converted to underscore

        public String publicField = "public"; // Testing public field parse without getter

        @FieldName(name = "new_field_name")
        private String mustBeRenamedField = "renamedField"; // Have to be renamed to "new_field_name"

        private String nullField; // Have to be ignored

        private String noGetterPrivateField = "No Getter"; // Have to be ignored

        RequestObjectMock() {
            super(ResponseObjectMock.class, null);
        }

        @Override
        protected HttpRequestBase getHttpRequestObject(String url) {
            return null;
        }

        public String getIgnoredField() {
            return ignoredField;
        }

        public String getCamelCaseField() {
            return camelCaseField;
        }

        public String getMustBeRenamedField() {
            return mustBeRenamedField;
        }

        public String getNullField() {
            return nullField;
        }

    }

}
