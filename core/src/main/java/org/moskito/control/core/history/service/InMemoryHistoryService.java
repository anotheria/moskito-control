package org.moskito.control.core.history.service;

import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.core.history.StatusUpdateHistoryItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryHistoryService implements HistoryService {
    private final List<StatusUpdateHistoryItem> items = new LinkedList<>();

    @Override
    public synchronized void addItem(StatusUpdateHistoryItem historyItem) {
        items.add(historyItem);
        while (items.size() > MoskitoControlConfiguration.getConfiguration().getHistoryItemsAmount()) {
            items.remove(0);
        }
    }

    @Override
    public List<StatusUpdateHistoryItem> getItems() {
        List<StatusUpdateHistoryItem> result = new ArrayList<>(items);
        Collections.reverse(result);
        return result;
    }

    @Override
    public List<StatusUpdateHistoryItem> getItemsByComponentName(String componentName) {
        List<StatusUpdateHistoryItem> result = new ArrayList<>(items)
                .stream()
                .filter((historyItem -> historyItem.getComponent().getName().equals(componentName)))
                .collect(Collectors.toList());

        Collections.reverse(result);
        return result;
    }

    @Override
    public List<StatusUpdateHistoryItem> getItemsByComponentNames(List<String> componentNames) {
        List<StatusUpdateHistoryItem> result = new ArrayList<>(items)
                .stream()
                .filter((historyItem -> componentNames.contains(historyItem.getComponent().getName())))
                .collect(Collectors.toList());

        Collections.reverse(result);
        return result;
    }
}
