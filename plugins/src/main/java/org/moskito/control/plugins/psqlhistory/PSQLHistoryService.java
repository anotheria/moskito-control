package org.moskito.control.plugins.psqlhistory;

import org.flywaydb.core.Flyway;
import org.moskito.control.common.HealthColor;
import org.moskito.control.common.Status;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.core.Component;
import org.moskito.control.core.ComponentRepository;
import org.moskito.control.core.history.StatusUpdateHistoryItem;
import org.moskito.control.core.history.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PSQLHistoryService implements HistoryService {
    private static final Logger log = LoggerFactory.getLogger(PSQLHistoryService.class);
    private final PSQLHistoryPluginConfig config;

    public PSQLHistoryService(PSQLHistoryPluginConfig config) {
        this.config = config;
        setUp();
    }

    private void setUp() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }

        Flyway flyway = Flyway.configure().dataSource(this.config.getDbUrl(), this.config.getDbUsername(), this.config.getDbPassword()).load();
        flyway.migrate();
    }

    @Override
    public void addItem(StatusUpdateHistoryItem historyItem) {
        String insertSql = "INSERT INTO history (component_name, old_status_value, old_status_messages, new_status_value, new_status_messages, timestamp) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(this.config.getDbUrl(), this.config.getDbUsername(), this.config.getDbPassword())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                preparedStatement.setString(1, historyItem.getComponent().getName());
                preparedStatement.setString(2, historyItem.getOldStatus().getHealth().getName());
                preparedStatement.setArray(3, connection.createArrayOf("TEXT", historyItem.getOldStatus().getMessages().toArray()));
                preparedStatement.setString(4, historyItem.getNewStatus().getHealth().getName());
                preparedStatement.setArray(5, connection.createArrayOf("TEXT", historyItem.getNewStatus().getMessages().toArray()));
                preparedStatement.setLong(6, historyItem.getTimestamp());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<StatusUpdateHistoryItem> getItems() {
        String selectSql = "SELECT * FROM history ORDER BY id DESC LIMIT ?";

        try (Connection connection = DriverManager.getConnection(this.config.getDbUrl(), this.config.getDbUsername(), this.config.getDbPassword())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSql)) {
                preparedStatement.setInt(1, MoskitoControlConfiguration.getConfiguration().getHistoryItemsAmount());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    List<StatusUpdateHistoryItem> result = new ArrayList<>();

                    while (resultSet.next()) {
                        StatusUpdateHistoryItem historyItem = convertResultSetToHistoryItem(resultSet);
                        result.add(historyItem);
                    }

                    return result;
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<StatusUpdateHistoryItem> getItemsByComponentName(String componentName) {
        String selectSql = "SELECT * FROM history WHERE component_name = ? ORDER BY id DESC LIMIT ?";

        try (Connection connection = DriverManager.getConnection(this.config.getDbUrl(), this.config.getDbUsername(), this.config.getDbPassword())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSql)) {
                preparedStatement.setString(1, componentName);
                preparedStatement.setInt(2, MoskitoControlConfiguration.getConfiguration().getHistoryItemsAmount());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    List<StatusUpdateHistoryItem> result = new ArrayList<>();

                    while (resultSet.next()) {
                        StatusUpdateHistoryItem historyItem = convertResultSetToHistoryItem(resultSet);
                        result.add(historyItem);
                    }

                    return result;
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<StatusUpdateHistoryItem> getItemsByComponentNames(List<String> componentNames) {
        if (componentNames.isEmpty()) {
            return new ArrayList<>();
        }

        String[] inClauseParameters = new String[componentNames.size()];
        Arrays.fill(inClauseParameters, "?");

        String selectSql = "SELECT * FROM history WHERE component_name IN (" + String.join(",", inClauseParameters) + ") ORDER BY id DESC LIMIT ?";

        try (Connection connection = DriverManager.getConnection(this.config.getDbUrl(), this.config.getDbUsername(), this.config.getDbPassword())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSql)) {
                int paramsIndex = 0;

                for (String componentName : componentNames) {
                    preparedStatement.setString(++paramsIndex, componentName);
                }

                preparedStatement.setInt(++paramsIndex, MoskitoControlConfiguration.getConfiguration().getHistoryItemsAmount());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    List<StatusUpdateHistoryItem> result = new ArrayList<>();

                    while (resultSet.next()) {
                        StatusUpdateHistoryItem historyItem = convertResultSetToHistoryItem(resultSet);
                        result.add(historyItem);
                    }

                    return result;
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private StatusUpdateHistoryItem convertResultSetToHistoryItem(ResultSet resultSet) throws SQLException {
        String componentName = resultSet.getString("component_name");
        String oldStatusValue = resultSet.getString("old_status_value");
        Array oldStatusMessages = resultSet.getArray("old_status_messages");
        String newStatusValue = resultSet.getString("new_status_value");
        Array newStatusMessages = resultSet.getArray("new_status_messages");
        long timestamp = resultSet.getLong("timestamp");

        Component component = ComponentRepository.getInstance().getComponent(componentName);
        Status oldStatus = new Status(HealthColor.forName(oldStatusValue), Arrays.asList((String[]) oldStatusMessages.getArray()));
        Status newStatus = new Status(HealthColor.forName(newStatusValue), Arrays.asList((String[]) newStatusMessages.getArray()));

        return new StatusUpdateHistoryItem(component, oldStatus, newStatus, timestamp);
    }
}
