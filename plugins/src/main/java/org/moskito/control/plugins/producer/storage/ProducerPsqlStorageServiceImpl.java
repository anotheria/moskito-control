package org.moskito.control.plugins.producer.storage;

import net.anotheria.util.queue.IQueueWorker;
import net.anotheria.util.queue.QueuedProcessor;
import net.anotheria.util.queue.UnrecoverableQueueOverflowException;
import org.flywaydb.core.Flyway;
import org.moskito.control.core.producer.storage.ProducerStorageItem;
import org.moskito.control.core.producer.storage.ProducerStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Properties;

/**
 * Implementation of the ProducerStorageService interface that stores items in a database.
 */
public class ProducerPsqlStorageServiceImpl implements ProducerStorageService {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(ProducerPsqlStorageServiceImpl.class);
    /**
     * Processor for handling items in a queue.
     */
    private final QueuedProcessor<ProducerStorageItem> itemsProcessor;

    /**
     * Constructs a new instance with the specified configuration.
     *
     * @param config The configuration for the storage service.
     */
    public ProducerPsqlStorageServiceImpl(ProducerStoragePluginConfig config) {
        Flyway flyway = Flyway.configure().baselineOnMigrate(true).table("flyway_producer_storage").dataSource(config.getDbUrl(), config.getDbUsername(), config.getDbPassword()).load();
        flyway.migrate();

        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.url", config.getDbUrl());
        properties.setProperty("hibernate.connection.username", config.getDbUsername());
        properties.setProperty("hibernate.connection.password", config.getDbPassword());

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ProducerStoragePlugin", properties);
        this.itemsProcessor = new QueuedProcessor<>("PsqlStorageItems-processor", new ProducerPsqlStorageItemsWorker(entityManagerFactory), 10000, 75, log);
        this.itemsProcessor.start();
    }

    @Override
    public void addItem(ProducerStorageItem item) {
        try {
            itemsProcessor.addToQueue(item);
        } catch (UnrecoverableQueueOverflowException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Worker for processing ProducerStorageItem objects.
     */
    private static class ProducerPsqlStorageItemsWorker implements IQueueWorker<ProducerStorageItem> {
        /**
         * Entity manager factory for managing entity operations.
         */
        private final EntityManagerFactory entityManagerFactory;
        /**
         * Constructs a new ProducerStorageItemsWorker instance with the specified entity manager factory.
         *
         * @param entityManagerFactory The entity manager factory to use for database operations.
         */
        private ProducerPsqlStorageItemsWorker(EntityManagerFactory entityManagerFactory) {
            this.entityManagerFactory = entityManagerFactory;
        }

        @Override
        public void doWork(ProducerStorageItem item) {
            EntityManager entityManager = entityManagerFactory.createEntityManager();

            try {
                entityManager.getTransaction().begin();
                entityManager.persist(ProducerStorageServiceConvertor.toDO(item));
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
