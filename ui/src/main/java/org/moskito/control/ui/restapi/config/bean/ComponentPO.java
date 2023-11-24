package org.moskito.control.ui.restapi.config.bean;

import org.moskito.control.config.ComponentConfig;
import org.moskito.control.config.ConnectorType;
import org.moskito.control.config.HeaderParameter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ComponentPO {

    private String name;

    private String category;

    private ConnectorType connectorType;

    private Map<String, String> data = new HashMap<>();

    private HeaderParameter[] headers;

    private String location;

    private String tags;

    private String credentials;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComponentPO that = (ComponentPO) o;
        return Objects.equals(name, that.name) && Objects.equals(category, that.category) && connectorType == that.connectorType && Objects.equals(data, that.data) && Arrays.equals(headers, that.headers) && Objects.equals(location, that.location) && Objects.equals(tags, that.tags) && Objects.equals(credentials, that.credentials);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, category, connectorType, data, location, tags, credentials);
        result = 31 * result + Arrays.hashCode(headers);
        return result;
    }

    @Override
    public String toString() {
        return "NewComponent{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", connectorType=" + connectorType +
                ", data=" + data +
                ", headers=" + Arrays.toString(headers) +
                ", location='" + location + '\'' +
                ", tags='" + tags + '\'' +
                ", credentials='" + credentials + '\'' +
                '}';
    }

    public ComponentConfig toComponentConfig(){
        ComponentConfig component = new ComponentConfig();
        component.setName(this.getName());
        component.setCategory(this.getCategory());
        component.setConnectorType(this.getConnectorType());
        component.setData(this.getData());
        component.setHeaders(this.getHeaders());
        component.setLocation(this.getLocation());
        component.setTags(this.getTags());
        component.setCredentials(this.getCredentials());
        return component;
    }
}
