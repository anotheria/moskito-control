package org.moskito.control.config;

import com.google.gson.annotations.SerializedName;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

/**
 * @author akulyk
 */
@ConfigureMe
public class HeaderParameter {

    /**
     * Header key
     */
    @Configure
    @SerializedName("key")
    private String key;

    /**
     * Header value
     */
    @Configure
    @SerializedName("value")
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "HeaderParameter{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
