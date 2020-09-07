package org.moskito.control.plugins.psqlhistory;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "history")
public class HistoryItemDO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "component_name")
    private String componentName;

    @Column(name = "old_status_value")
    private String oldStatusName;

    @Column(name = "old_status_messages")
    @Convert(converter = HistoryItemDOStatusMessagesConverter.class)
    private List<String> oldStatusMessages;

    @Column(name = "new_status_value")
    private String newStatusName;

    @Column(name = "new_status_messages")
    @Convert(converter = HistoryItemDOStatusMessagesConverter.class)
    private List<String> newStatusMessages;

    @Column(name = "timestamp")
    private long timestamp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getOldStatusName() {
        return oldStatusName;
    }

    public void setOldStatusName(String oldStatusName) {
        this.oldStatusName = oldStatusName;
    }

    public List<String> getOldStatusMessages() {
        return oldStatusMessages;
    }

    public void setOldStatusMessages(List<String> oldStatusMessages) {
        this.oldStatusMessages = oldStatusMessages;
    }

    public String getNewStatusName() {
        return newStatusName;
    }

    public void setNewStatusName(String newStatusName) {
        this.newStatusName = newStatusName;
    }

    public List<String> getNewStatusMessages() {
        return newStatusMessages;
    }

    public void setNewStatusMessages(List<String> newStatusMessages) {
        this.newStatusMessages = newStatusMessages;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
