package org.moskito.control.data.thresholds;

import org.moskito.control.core.HealthColor;

/**
 * Guard for a single threshold status in the threshold (From the config file).
 */
public class GuardConfig {
    /**
     * Direction in which the value has to be passed (UP/DOWN).
     */
    private GuardDirection direction;
    
    /**
     * Value that have to be reached for the status change.
     */
    private String value;

    /**
     * The color for reached value.
     */
    private HealthColor color;

    public GuardDirection getDirection() {
        return direction;
    }

    public void setDirection(GuardDirection direction) {
        this.direction = direction;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public HealthColor getColor() {
        return color;
    }

    public void setColor(HealthColor color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "{direction: " + getDirection() + ", value: " + getValue() + ", color: " + getColor() + "}";
    }
}
