package org.moskito.control.connectors.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Info provider for PostgreSQL.
 * Retrieves uptime info
 */
public class PostgreSQLInfoProvider implements InfoProvider {

    /**
     * Query to get uptime seconds
     */
    private static final String UPTIME_QUERY =
            "SELECT EXTRACT(EPOCH FROM (now() - pg_postmaster_start_time())) as uptime;";

    @Override
    public Map<String, String> getInfo(Connection connection, int timeout) throws SQLException {

        Map<String, String> info = new HashMap<>();

        ResultSet rs = connection.prepareStatement(UPTIME_QUERY).executeQuery();
        rs.next();

        long uptime = ((Number) rs.getObject(1)).longValue();

        info.put("Uptime", String.valueOf(uptime) );

        info.put("Uphours",
                String.valueOf( uptime / 3600 )
        );
        info.put("Updays",
                String.valueOf( uptime / 3600 / 24 )
        );

        return info;

    }

}
