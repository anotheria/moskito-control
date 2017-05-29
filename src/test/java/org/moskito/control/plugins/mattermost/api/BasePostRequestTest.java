package org.moskito.control.plugins.mattermost.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BasePostRequestTest {

    public String getExpectedJson(){

        ObjectNode expectedJson = new ObjectNode(JsonNodeFactory.instance);
        expectedJson.set("field_two", TextNode.valueOf("field2Value"));
        expectedJson.set("field_one", TextNode.valueOf("field1Value"));

        return expectedJson.toString();

    }

    /**
     * Tests converting BasePostRequest object fields to json
     */
    @Test public void testToJson(){

        BasePostRequestMock testRequest = new BasePostRequestMock();

        try {
            assertEquals(getExpectedJson(), testRequest.toJson());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    static class BaseResponseMock extends BaseResponse{}

    public static class BasePostRequestMock extends BasePostRequest<BaseResponseMock>{

        private String fieldOne = "field1Value";
        private String fieldTwo = "field2Value";

        public String getFieldOne() {
            return fieldOne;
        }

        public String getFieldTwo() {
            return fieldTwo;
        }

        BasePostRequestMock() {
            super(BaseResponseMock.class, null);
        }

    }

}
