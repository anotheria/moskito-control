package org.moskito.control.plugins.escalation;

import org.moskito.control.common.Status;

public class ComponentStatusHolder {
    private String componentName;

    private Status status;

    private String message;

    private int ticks;

    public ComponentStatusHolder(String aName, Status aStatus){
        componentName = aName;
        status = aStatus;
        ticks = 1;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public void increaseTicks() {
        ticks++;
    }

    @Override
    public String toString() {
        return "ComponentStatusHolder{" +
                "componentName='" + componentName + '\'' +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", ticks=" + ticks +
                '}';
    }
}
