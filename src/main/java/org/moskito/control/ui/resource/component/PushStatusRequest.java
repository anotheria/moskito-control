package org.moskito.control.ui.resource.component;

import java.io.Serializable;
import java.util.List;

import org.moskito.control.core.status.Status;

/**
 * @author
 */
public class PushStatusRequest implements Serializable {

    private String name;
    private Status status;
    private List<String> tags;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "PushStatusRequest{" + "name='" + name + '\'' + ", status=" + status + ", tags=" + tags + '}';
    }
}
