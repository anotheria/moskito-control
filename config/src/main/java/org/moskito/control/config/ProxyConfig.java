package org.moskito.control.config;

import com.google.gson.annotations.SerializedName;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

@ConfigureMe
public class ProxyConfig {

    @Configure
    @SerializedName("name") private String name;

    @Configure
    @SerializedName("prefix") private String prefix;

    @Configure
    @SerializedName("url") private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ProxyConfig{" +
                "name='" + name + '\'' +
                ", prefix='" + prefix + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
