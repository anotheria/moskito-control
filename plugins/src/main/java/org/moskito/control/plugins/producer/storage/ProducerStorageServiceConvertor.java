package org.moskito.control.plugins.producer.storage;

import org.moskito.control.core.producer.storage.ProducerStorageItem;

import java.util.UUID;

/**
 * Utility class for converting between ProducerStorageItem and AbstractProducerStorageItemDO objects.
 */
public final class ProducerStorageServiceConvertor {

    /**
     * Converts a ProducerStorageItem object to an AbstractProducerStorageItemDO object.
     *
     * @param item The ProducerStorageItem object to convert.
     * @return The converted AbstractProducerStorageItemDO object.
     */
    public static AbstractProducerStorageItemDO toDO(ProducerStorageItem item) {
        AbstractProducerStorageItemDO result;
        switch (item.getInterval()) {
            case "1m":
                result = new ProducerStorageItemDO1m();
                break;
            case "5m":
                result = new ProducerStorageItemDO5m();
                break;
            case "15m":
                result = new ProducerStorageItemDO15m();
                break;
            case "1h":
            default:
                result = new ProducerStorageItemDO1h();

        }
        result.setId(UUID.randomUUID().toString());
        result.setProducerId(item.getProducerId());
        result.setTt(item.getTt());
        result.setLast(item.getLast());
        result.setMin(item.getMin());
        result.setMax(item.getMax());
        result.setAvg(item.getAvg());
        result.setErr(item.getErr());
        result.setMcr(item.getMcr());
        result.setTr(item.getTr());
        result.setCr(item.getCr());
        result.setTimestamp(item.getTimestamp());
        return result;
    }

    /**
     * Private constructor to prevent instantiation of the utility class.
     */
    private ProducerStorageServiceConvertor() {
        throw new IllegalAccessError("Can't be instantiated.");
    }
}
