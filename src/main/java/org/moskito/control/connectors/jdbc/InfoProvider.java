package org.moskito.control.connectors.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * Interface to provide specific database info for JDBC connector
 */
public interface InfoProvider {

    Map<String, String> getInfo(Connection connection, int timeout) throws SQLException;

}
