package org.moskito.control.connectors.jdbc;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages JDBC info provider.
 * Stores providers by database names
 */
public class InfoProviderManager {

    /**
     * DB name and info provider instances aliases
     */
    private static Map<String, InfoProvider> infoProviders = new HashMap<>();

    static {
        addInfoProvider("mysql", new MySQLInfoProvider());
        addInfoProvider("postgresql", new PostgreSQLInfoProvider());
    }

    /**
     * Adds info provider to provider manager.
     *
     * @param dbName name of database provider related to
     * @param provider info provider instance for specified database
     */
    public static void addInfoProvider(String dbName, InfoProvider provider){
        infoProviders.put(dbName, provider);
    }

    /**
     * Returns info provider instance for specified database.
     * Returns default, if no such db present
     *
     * @param dbName name of database
     * @return info provider instance
     */
    public static InfoProvider getFor(String dbName){
            return infoProviders.get(dbName);
    }

}
