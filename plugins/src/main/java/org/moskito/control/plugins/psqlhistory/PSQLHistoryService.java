package org.moskito.control.plugins.psqlhistory;

import net.anotheria.util.queue.IQueueWorker;
import net.anotheria.util.queue.QueuedProcessor;
import net.anotheria.util.queue.UnrecoverableQueueOverflowException;
import org.flywaydb.core.Flyway;
import org.moskito.control.config.MoskitoControlConfiguration;
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
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

public class PSQLHistoryService implements HistoryService {
    private static final Logger log = LoggerFactory.getLogger(PSQLHistoryService.class);
    private final EntityManagerFactory entityManagerFactory;
    private final QueuedProcessor<StatusUpdateHistoryItem> historyItemsProcessor;

    public PSQLHistoryService(PSQLHistoryPluginConfig config) {
        Flyway flyway = Flyway.configure().dataSource(config.getDbUrl(), config.getDbUsername(), config.getDbPassword()).load();
        flyway.migrate();

        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.url", config.getDbUrl());
        properties.setProperty("hibernate.connection.username", config.getDbUsername());
        properties.setProperty("hibernate.connection.password", config.getDbPassword());

        this.entityManagerFactory = Persistence.createEntityManagerFactory("PSQLHistoryPlugin-history", properties);
        this.historyItemsProcessor = new QueuedProcessor<>("HistoryItems-processor", new StatusUpdateHistoryItemsWorker(entityManagerFactory), 10000, 75, log);
        this.historyItemsProcessor.start();
    }

    @Override
    public void addItem(StatusUpdateHistoryItem historyItem) {
        try {
            historyItemsProcessor.addToQueue(historyItem);
        } catch (UnrecoverableQueueOverflowException e) {
            log.error(e.getMessage(), e);
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

        return historyItemDOs.stream().map(PSQLHistoryServiceConvertor::fromDO).filter(Objects::nonNull).collect(Collectors.toList());
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

        return historyItemDOs.stream().map(PSQLHistoryServiceConvertor::fromDO).filter(Objects::nonNull).collect(Collectors.toList());
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

        return historyItemDOs.stream().map(PSQLHistoryServiceConvertor::fromDO).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private static class StatusUpdateHistoryItemsWorker implements IQueueWorker<StatusUpdateHistoryItem> {
        private final EntityManagerFactory entityManagerFactory;

        public StatusUpdateHistoryItemsWorker(EntityManagerFactory entityManagerFactory) {
            this.entityManagerFactory = entityManagerFactory;
        }

        @Override
        public void doWork(StatusUpdateHistoryItem historyItem) throws Exception {
            EntityManager entityManager = entityManagerFactory.createEntityManager();

            try {
                entityManager.getTransaction().begin();
                entityManager.persist(PSQLHistoryServiceConvertor.toDO(historyItem));
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                entityManager.getTransaction().rollback();
            } finally {
                entityManager.close();
            }
        }
    }
}
