package org.moskito.control.plugins.psqlhistory;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.List;

public class HistoryItemDOStatusMessagesConverter implements AttributeConverter<List<String>, String> {
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
