package org.moskito.control.config;

import com.google.gson.annotations.SerializedName;
import org.configureme.annotations.ConfigureMe;

/**
 * @author asamoilich.
 */
@ConfigureMe
public class ActionParameter {
    /**
     * Key of the parameter.
     */
    @SerializedName("key")
    private String key;
    /**
     * value of the parameter.
     */
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
}
