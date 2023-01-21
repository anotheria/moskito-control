package org.moskito.control.plugins.escalation;

import org.configureme.annotations.Configure;

public class EscalationConfigEntry {
    @Configure
    private int ticks;
    @Configure
    private String message;

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "EscalationConfigEntry{" +
                "ticks=" + ticks +
                ", message='" + message + '\'' +
                '}';
    }
}
