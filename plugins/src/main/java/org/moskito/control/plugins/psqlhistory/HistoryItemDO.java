package org.moskito.control.plugins.psqlhistory;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Arrays;
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
    private String oldStatusValue;

    @Column(name = "old_status_messages")
    @Convert(converter = StatusMessagesConverter.class)
    private List<String> oldStatusMessages;

    @Column(name = "new_status_value")
    private String newStatusValue;

    @Column(name = "new_status_messages")
    @Convert(converter = StatusMessagesConverter.class)
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

    public String getOldStatusValue() {
        return oldStatusValue;
    }

    public void setOldStatusValue(String oldStatusName) {
        this.oldStatusValue = oldStatusName;
    }

    public List<String> getOldStatusMessages() {
        return oldStatusMessages;
    }

    public void setOldStatusMessages(List<String> oldStatusMessages) {
        this.oldStatusMessages = oldStatusMessages;
    }

    public String getNewStatusValue() {
        return newStatusValue;
    }

    public void setNewStatusValue(String newStatusName) {
        this.newStatusValue = newStatusName;
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

    private static class StatusMessagesConverter implements AttributeConverter<List<String>, String> {
        private final String SEPARATOR = "///";

        @Override
        public String convertToDatabaseColumn(List<String> messages) {
            return String.join(SEPARATOR, messages);
        }

        @Override
        public List<String> convertToEntityAttribute(String messagesString) {
            return Arrays.asList(messagesString.split(SEPARATOR));
        }
    }
}
