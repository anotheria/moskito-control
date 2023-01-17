package org.moskito.control.config;

import com.google.gson.annotations.SerializedName;
import net.anotheria.util.StringUtils;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration of a component.
 *
 * @author lrosenberg
 * @since 26.02.13 01:33
 */
@ConfigureMe
public class ComponentConfig {
    private static final int COMPANY_NAME_LIMIT = 100;

    /**
     * Name of the component.
     */
    @Configure
    @SerializedName("name")
    private String name;

    /**
     * Category.
     */
    @Configure
    @SerializedName("category")
    private String category;

    /**
     * Type of the connector for this component.
     */
    @Configure
    @SerializedName("connectorType")
    private ConnectorType connectorType;

    /**
     * Type of method if component is a URL-connector.
     * If component is non URL-connector, field is ignored.
     */
    @Configure
    @SerializedName("methodType")
    private HttpMethodType methodType;

    /**
     * Payload. If a provided method type above accepts payload (POST, PUT etc.)
     */
    @Configure
    @SerializedName("payload")
    private String payload;

    /**
     * Content type. Field is required if payload was provided.
     * Only text-friendly content type.
     * Example: application/json, application/xml.
     */
    @Configure
    @SerializedName("contentType")
    private String contentType;

    /**
     * Headers map. Used with HttpUrlConnector.
     */
    @Configure
    @SerializedName("@headers")
    private HeaderParameter[] headers;

    /**
     * Connector specific location.
     */
    @Configure
    @SerializedName("location")
    private String location;

    /**
     * Component tags.
     */
    @Configure
    @SerializedName("tags")
    private String tags;

    /**
     * Connector specific credentials.
     */
    @Configure
    private String credentials;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        boolean isLengthLimitReached = !StringUtils.isEmpty(name) && name.length() > COMPANY_NAME_LIMIT;
        this.name = isLengthLimitReached ? name.substring(0, COMPANY_NAME_LIMIT) : name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ConnectorType getConnectorType() {
        return connectorType;
    }

    public void setConnectorType(ConnectorType connectorType) {
        this.connectorType = connectorType;
    }

    public HttpMethodType getMethodType() {
        return methodType;
    }

    public void setMethodType(HttpMethodType methodType) {
        this.methodType = methodType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public HeaderParameter[] getHeaders() {
        return headers;
    }

    public void setHeaders(HeaderParameter[] headers) {
        this.headers = headers;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "ComponentConfig{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", connectorType=" + connectorType +
                ", methodType=" + methodType +
                ", payload='" + payload + '\'' +
                ", contentType='" + contentType + '\'' +
                ", headers=" + Arrays.toString(headers) +
                ", location='" + location + '\'' +
                ", tags='" + tags + '\'' +
                ", credentials='" + credentials + '\'' +
                '}';
    }
}
