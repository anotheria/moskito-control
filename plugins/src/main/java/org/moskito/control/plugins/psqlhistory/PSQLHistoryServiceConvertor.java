package org.moskito.control.plugins.psqlhistory;

import org.moskito.control.common.HealthColor;
import org.moskito.control.common.Status;
import org.moskito.control.core.Component;
import org.moskito.control.core.Repository;
import org.moskito.control.core.history.StatusUpdateHistoryItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PSQLHistoryServiceConvertor {
    private static final Logger log = LoggerFactory.getLogger(PSQLHistoryServiceConvertor.class);

    public static StatusUpdateHistoryItem fromDO(HistoryItemDO historyItem) {
        Component component = Repository.getInstance().getComponent(historyItem.getComponentName());
        if (component == null) {
            log.warn(String.format("Component repository doesn't contain component with name '%s'. Check your configuration.", historyItem.getComponentName()));
            return null;
        }

        Status oldStatus = new Status(HealthColor.forName(historyItem.getOldStatusValue()), historyItem.getOldStatusMessages());
        Status newStatus = new Status(HealthColor.forName(historyItem.getNewStatusValue()), historyItem.getNewStatusMessages());

        return new StatusUpdateHistoryItem(component, oldStatus, newStatus, historyItem.getTimestamp());
    }

    public static HistoryItemDO toDO(StatusUpdateHistoryItem historyItem) {
        HistoryItemDO result = new HistoryItemDO();
        result.setComponentName(historyItem.getComponent().getName());
        result.setOldStatusValue(historyItem.getOldStatus().getHealth().getName());
        result.setOldStatusMessages(historyItem.getOldStatus().getMessages());
        result.setNewStatusValue(historyItem.getNewStatus().getHealth().getName());
        result.setNewStatusMessages(historyItem.getNewStatus().getMessages());
        result.setTimestamp(historyItem.getTimestamp());
        return result;
    }


    private PSQLHistoryServiceConvertor() {
        throw new IllegalAccessError("Can't be instantiated.");
    }
}
