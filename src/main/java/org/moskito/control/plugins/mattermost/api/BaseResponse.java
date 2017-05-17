package org.moskito.control.plugins.mattermost.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Basic response class for Mattermost API methods.
 * Contains method for populating object from response json.
 *
 * All child final classes must have public constructor with no arguments
 * because response classes instances created after request via reflection
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true) // Mattermost API documentation seems not covers all response fields
public abstract class BaseResponse {

    /**
     * Populates object from Mattermost API response json.
     * @param response Http Response
     * @throws IOException thrown by Jackson on invalid json
     *                     or by Apache library if there is problems creating stream
     * @throws JsonProcessingException thrown by Jackson on invalid json
     */
    public void populateResponse(HttpResponse response) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.readerForUpdating(this)
                .readValue(new InputStreamReader(response.getEntity().getContent()));

    }

}
