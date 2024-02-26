package org.moskito.control.plugins.producer.storage;

import net.anotheria.util.queue.IQueueWorker;
import net.anotheria.util.queue.QueuedProcessor;
import net.anotheria.util.queue.UnrecoverableQueueOverflowException;
import org.moskito.control.core.producer.storage.ProducerStorageItem;
import org.moskito.control.core.producer.storage.ProducerStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Implementation of the ProducerStorageService interface that stores items in a file.
 */
public class ProducerFileStorageServiceImpl implements ProducerStorageService {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(ProducerFileStorageServiceImpl.class);

    /**
     * Csv header.
     */
    private static final String HEADER_CSV = "producer_id,tt,last,min,max,avg,err,mcr,tr,cr,timestamp\n";

    /**
     * Processor for handling items in a queue.
     */
    private final QueuedProcessor<ProducerStorageItem> itemsProcessor;

    /**
     * Constructs a new instance with the specified configuration.
     *
     * @param config The configuration for the storage service.
     */
    public ProducerFileStorageServiceImpl(ProducerStoragePluginConfig config) {
        this.itemsProcessor = new QueuedProcessor<>("FsStorageItems-processor", new ProducerFsStorageItemsWorker(config.getStorageDirectory()), 10000, 75, log);
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
     * Worker class for processing ProducerStorageItem objects.
     */
    private static class ProducerFsStorageItemsWorker implements IQueueWorker<ProducerStorageItem> {

        /**
         * Storage directory.
         */
        private final String storageDirectory;

        private ProducerFsStorageItemsWorker(String storageDirectory) {
            this.storageDirectory = storageDirectory;
        }

        @Override
        public void doWork(ProducerStorageItem item) {
            String fileName = "producer_storage_" + item.getInterval() + ".csv";
            StringBuilder line = new StringBuilder(item.getProducerId());
            line.append(",\"").append(item.getTt()).append("\",");
            line.append("\"").append(item.getLast()).append("\",");
            line.append("\"").append(item.getMin()).append("\",");
            line.append("\"").append(item.getMax()).append("\",");
            line.append(item.getAvg()).append(",");
            line.append(item.getErr()).append(",");
            line.append(item.getMcr()).append(",");
            line.append(item.getTr()).append(",");
            line.append(item.getCr()).append(",");
            line.append(item.getTimestamp()).append("\n");
            Path filePath = Paths.get(storageDirectory, fileName);
            try {
                if (!Files.exists(filePath)) {
                    Files.createDirectories(filePath.getParent());
                    Files.createFile(filePath);
                    Files.writeString(filePath, HEADER_CSV, StandardOpenOption.APPEND);
                }
                Files.writeString(filePath, line, StandardOpenOption.APPEND);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }
}
