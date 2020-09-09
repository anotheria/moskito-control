package org.moskito.control.core.history.service;

import org.moskito.control.core.history.StatusUpdateHistoryItem;

import java.util.List;

public interface HistoryService {
    void addItem(StatusUpdateHistoryItem historyItem);

    List<StatusUpdateHistoryItem> getItems();

    List<StatusUpdateHistoryItem> getItemsByComponentName(String componentName);

    List<StatusUpdateHistoryItem> getItemsByComponentNames(List<String> componentNames);
}
