package org.moskito.control.plugins.producer.storage;

import org.moskito.control.core.producer.storage.ProducerStorageService;

/**
 * Factory class for creating instances of ProducerStorageService based on the provided configuration.
 */
public class ProducerStorageServiceFactory {
    /**
     * Creates and returns an instance of ProducerStorageService based on the provided configuration.
     *
     * @param config The configuration for creating the storage service.
     * @return An instance of ProducerStorageService.
     * @throws IllegalArgumentException if the storage type specified in the configuration is unsupported.
     */
    public static ProducerStorageService get(ProducerStoragePluginConfig config) throws Exception {
        switch (StorageType.getByName(config.getStorageType())) {
            case PSQL:
                return new ProducerPsqlStorageServiceImpl(config);
            case FILE:
                return new ProducerFileStorageServiceImpl(config);
            case UNKNOWN:
            default:
                throw new IllegalArgumentException("Unsupported storage type: " + config.getStorageType());
        }
    }

}
