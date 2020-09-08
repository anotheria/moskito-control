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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class PSQLHistoryService implements HistoryService {
    private static final Logger log = LoggerFactory.getLogger(PSQLHistoryService.class);
    private final EntityManagerFactory entityManagerFactory;

    public PSQLHistoryService(PSQLHistoryPluginConfig config) {
        Flyway flyway = Flyway.configure().dataSource(config.getDbUrl(), config.getDbUsername(), config.getDbPassword()).load();
        flyway.migrate();

        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.url", config.getDbUrl());
        properties.setProperty("hibernate.connection.username", config.getDbUsername());
        properties.setProperty("hibernate.connection.password", config.getDbPassword());

        this.entityManagerFactory = Persistence.createEntityManagerFactory("PSQLHistoryPlugin-history", properties);
    }

    @Override
    public void addItem(StatusUpdateHistoryItem historyItem) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        HistoryItemDO historyItemDO = this.convertHistoryItemToDO(historyItem);

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(historyItemDO);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<StatusUpdateHistoryItem> getItems() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<HistoryItemDO> criteriaQuery = criteriaBuilder.createQuery(HistoryItemDO.class);
        Root<HistoryItemDO> from = criteriaQuery.from(HistoryItemDO.class);
        criteriaQuery.select(from);
        criteriaQuery.orderBy(criteriaBuilder.desc(from.get("timestamp")));

        List<HistoryItemDO> historyItemDOs = entityManager.createQuery(criteriaQuery)
                .setMaxResults(MoskitoControlConfiguration.getConfiguration().getHistoryItemsAmount()).getResultList();

        return historyItemDOs.stream().map(this::convertHistoryItemFromDO).collect(Collectors.toList());
    }

    @Override
    public List<StatusUpdateHistoryItem> getItemsByComponentName(String componentName) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<HistoryItemDO> criteriaQuery = criteriaBuilder.createQuery(HistoryItemDO.class);
        Root<HistoryItemDO> from = criteriaQuery.from(HistoryItemDO.class);
        criteriaQuery.select(from);
        criteriaQuery.where(criteriaBuilder.equal(from.get("componentName"), componentName));
        criteriaQuery.orderBy(criteriaBuilder.desc(from.get("timestamp")));

        List<HistoryItemDO> historyItemDOs = entityManager.createQuery(criteriaQuery)
                .setMaxResults(MoskitoControlConfiguration.getConfiguration().getHistoryItemsAmount()).getResultList();

        return historyItemDOs.stream().map(this::convertHistoryItemFromDO).collect(Collectors.toList());
    }

    @Override
    public List<StatusUpdateHistoryItem> getItemsByComponentNames(List<String> componentNames) {
        if (componentNames.isEmpty()) {
            return new ArrayList<>();
        }

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<HistoryItemDO> criteriaQuery = criteriaBuilder.createQuery(HistoryItemDO.class);
        Root<HistoryItemDO> from = criteriaQuery.from(HistoryItemDO.class);
        criteriaQuery.select(from);
        criteriaQuery.where(from.get("componentName").in(componentNames));
        criteriaQuery.orderBy(criteriaBuilder.desc(from.get("timestamp")));

        List<HistoryItemDO> historyItemDOs = entityManager.createQuery(criteriaQuery)
                .setMaxResults(MoskitoControlConfiguration.getConfiguration().getHistoryItemsAmount()).getResultList();

        return historyItemDOs.stream().map(this::convertHistoryItemFromDO).collect(Collectors.toList());
    }

    private HistoryItemDO convertHistoryItemToDO(StatusUpdateHistoryItem historyItem) {
        HistoryItemDO result = new HistoryItemDO();
        result.setComponentName(historyItem.getComponent().getName());
        result.setOldStatusValue(historyItem.getOldStatus().getHealth().getName());
        result.setOldStatusMessages(historyItem.getOldStatus().getMessages());
        result.setNewStatusValue(historyItem.getNewStatus().getHealth().getName());
        result.setNewStatusMessages(historyItem.getNewStatus().getMessages());
        result.setTimestamp(historyItem.getTimestamp());
        return result;
    }

    private StatusUpdateHistoryItem convertHistoryItemFromDO(HistoryItemDO historyItem) {
        Component component = ComponentRepository.getInstance().getComponent(historyItem.getComponentName());
        Status oldStatus = new Status(HealthColor.forName(historyItem.getOldStatusValue()), historyItem.getOldStatusMessages());
        Status newStatus = new Status(HealthColor.forName(historyItem.getNewStatusValue()), historyItem.getNewStatusMessages());

        return new StatusUpdateHistoryItem(component, oldStatus, newStatus, historyItem.getTimestamp());
    }
}
