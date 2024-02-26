package org.moskito.control.plugins.producer.storage;

import net.anotheria.moskito.core.snapshot.ProducerSnapshot;
import net.anotheria.moskito.core.snapshot.SnapshotConsumer;
import net.anotheria.moskito.core.snapshot.SnapshotRepository;
import net.anotheria.moskito.core.snapshot.StatSnapshot;
import org.configureme.ConfigurationManager;
import org.moskito.control.connectors.HttpURLConnector;
import org.moskito.control.core.producer.storage.ProducerStorageItem;
import org.moskito.control.core.producer.storage.ProducerStorageService;
import org.moskito.control.plugins.AbstractMoskitoControlPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;

/**
 * A plugin for storing producer snapshots either in files or in a database.
 * This plugin implements the {@link SnapshotConsumer} interface to consume
 * producer snapshots and store them based on the configured storage type.
 */
public class ProducerStoragePlugin extends AbstractMoskitoControlPlugin {

    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(ProducerStoragePlugin.class);
    /**
     * The name of the configuration for this plugin.
     */
    private String configurationName;

    @Override
    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }

    @Override
    public void initialize() {
        ProducerStoragePluginConfig config = new ProducerStoragePluginConfig();
        ConfigurationManager.INSTANCE.configureAs(config, configurationName);
        ProducerStorageService service;
        try {
            service = ProducerStorageServiceFactory.get(config);
        } catch (Exception e) {
            log.error(e.getMessage());
            return;
        }

        SnapshotConsumer consumer = producerSnapshot -> {
            if (HttpURLConnector.USE_CASE_CATEGORY.equals(producerSnapshot.getCategory())) {
                service.addItem(convert(producerSnapshot));
            }
        };
        SnapshotRepository.getInstance().addConsumer(consumer);
    }

    /**
     * Converts a producer snapshot into a producer storage item object.
     *
     * @param producerSnapshot The producer snapshot to be converted.
     * @return A producer storage item object containing data from the producer snapshot.
     */
    private ProducerStorageItem convert(ProducerSnapshot producerSnapshot) {
        StatSnapshot statSnapshot = producerSnapshot.getStatSnapshot("GET");
        ProducerStorageItem item = new ProducerStorageItem();
        item.setProducerId(producerSnapshot.getProducerId());
        item.setTt(convertNanoToMilliSeconds(statSnapshot.getValue("TT")));
        item.setLast(convertNanoToMilliSeconds(statSnapshot.getValue("Last")));
        item.setMin(convertNanoToMilliSeconds(statSnapshot.getValue("Min")));
        item.setMax(convertNanoToMilliSeconds(statSnapshot.getValue("Max")));
        item.setAvg(convertAvgToMilliSeconds(statSnapshot.getValue("Avg")));
        item.setErr(statSnapshot.getValue("ERR"));
        item.setMcr(statSnapshot.getValue("MCR"));
        item.setTr(statSnapshot.getValue("TR"));
        item.setCr(statSnapshot.getValue("CR"));
        item.setTimestamp(producerSnapshot.getTimestamp());
        item.setInterval(producerSnapshot.getIntervalName());
        return item;
    }

    /**
     * Converts a string representing average time in nanoseconds to milliseconds.
     *
     * @param source The string representing the average time in nanoseconds.
     * @return A string representing the average time in milliseconds.
     */
    private String convertAvgToMilliSeconds(String source) {
        double nanoseconds = Double.parseDouble(source);
        double milliseconds = nanoseconds / 1_000_000;
        DecimalFormat df = new DecimalFormat("#.###");
        return df.format(milliseconds);
    }

    /**
     * Converts a string representing time in nanoseconds to milliseconds.
     *
     * @param source The string representing the time in nanoseconds.
     * @return A string representing the time in milliseconds.
     */
    private String convertNanoToMilliSeconds(String source) {
        long nanoseconds = Long.parseLong(source);
        double milliseconds = nanoseconds / 1_000_000.0;
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(milliseconds);
    }
}
