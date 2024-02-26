package org.moskito.control.plugins.producer.storage;

/**
 * Enumeration representing different types of storage.
 *
 * @author asamoilich.
 */
public enum StorageType {
    /**
     * File storage type.
     */
    FILE("file"),

    /**
     * PostgreSQL storage type.
     */
    PSQL("psql"),
    /**
     * Unknown storage type.
     */
    UNKNOWN("unknown");

    /**
     * The name of the storage type.
     */
    private final String typeName;

    /**
     * Constructs a new StorageType enum constant with the specified type name.
     *
     * @param typeName The name of the storage type.
     */
    StorageType(String typeName) {
        this.typeName = typeName;
    }

    /**
     * Returns the name of the storage type.
     *
     * @return The name of the storage type.
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Returns the StorageType enum constant with the specified type name.
     * If no match is found, returns the default PSQL type.
     *
     * @param typeName The name of the storage type.
     * @return The StorageType enum constant.
     */
    public static StorageType getByName(String typeName) {
        for (StorageType type : StorageType.values()) {
            if (type.typeName.equals(typeName)) {
                return type;
            }
        }
        return UNKNOWN;
    }
}

