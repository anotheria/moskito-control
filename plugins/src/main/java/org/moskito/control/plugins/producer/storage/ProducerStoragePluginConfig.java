package org.moskito.control.plugins.producer.storage;

import org.configureme.annotations.ConfigureMe;

@ConfigureMe(allfields = true)
public class ProducerStoragePluginConfig {
    /**
     * The type of storage to be used.
     */
    private String storageType;

    /**
     * The directory where CSV files will be stored (if using FILE storage).
     */
    private String storageDirectory;

    /**
     * The URL of the database (if using PSQL storage).
     */
    private String dbUrl;

    /**
     * The username for connecting to the database (if using PSQL storage).
     */
    private String dbUsername;

    /**
     * The password for connecting to the database (if using PSQL storage).
     */
    private String dbPassword;

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getStorageDirectory() {
        return storageDirectory;
    }

    public void setStorageDirectory(String storageDirectory) {
        this.storageDirectory = storageDirectory;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }
}
