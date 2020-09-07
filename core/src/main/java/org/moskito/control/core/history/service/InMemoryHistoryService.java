package org.moskito.control.core.history.service;

import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.core.history.StatusUpdateHistoryItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class InMemoryHistoryService implements HistoryService {
    private List<StatusUpdateHistoryItem> items = new CopyOnWriteArrayList<>();

    @Override
    public void addItem(StatusUpdateHistoryItem historyItem) {
        items.add(historyItem);

        int maxAllowedSize = (int) (MoskitoControlConfiguration.getConfiguration().getHistoryItemsAmount() * 1.10);
        if (items.size() > maxAllowedSize) {
            int fromIndex = Math.max(items.size() - 1 - MoskitoControlConfiguration.getConfiguration().getHistoryItemsAmount(), 0);
            int toIndex = items.size();
            StatusUpdateHistoryItem[] subList = Arrays.copyOfRange(items.toArray(new StatusUpdateHistoryItem[0]), fromIndex, toIndex);

            this.items = new CopyOnWriteArrayList<>(subList);
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
