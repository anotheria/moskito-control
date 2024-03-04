package org.moskito.control.core.producer.storage;

/**
 * Interface for a service responsible for adding ProducerStorageItem objects.
 */
public interface ProducerStorageService {
    /**
     * Adds a ProducerStorageItem to the storage.
     *
     * @param item The ProducerStorageItem to add.
     */
    void addItem(ProducerStorageItem item);

}
