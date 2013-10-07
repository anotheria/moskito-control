package org.moskito.control.core;

/**
 * Component status change event.
 */
public class StatusChangeEvent {
    /**
     * Application to which the component belongs.
     */
    private final Application application;

    /**
     * Component that changed the status.
     */
    private final Component component;

    /**
     * Old status of the component.
     */
    private final Status oldStatus;

    /**
     * New status of the component.
     */
    private final Status status;

    /**
     * Timestamp at which the status change took place.
     */
    private final long timestamp;

    /**
     * @param application application to which the component belongs.
     * @param component component that changed the status.
     * @param oldStatus old status of the component.
     * @param status new status of the component.
     * @param timestamp timestamp at which the status change took place.
     */
    public StatusChangeEvent(Application application, Component component, Status oldStatus, Status status, long timestamp) {
        this.application = application;
        this.component = component;
        this.oldStatus = oldStatus;
        this.status = status;
        this.timestamp = timestamp;
    }

    public Application getApplication() {
        return application;
    }

    public Component getComponent() {
        return component;
    }

    public Status getOldStatus() {
        return oldStatus;
    }

    public Status getStatus() {
        return status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StatusChangeEvent{");
        sb.append("application=").append(application);
        sb.append(", component=").append(component);
        sb.append(", oldStatus=").append(oldStatus);
        sb.append(", status=").append(status);
        sb.append(", timestamp=").append(timestamp);
        sb.append('}');
        return sb.toString();
    }
}
