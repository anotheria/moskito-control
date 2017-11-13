package org.moskito.control.connectors.jdbc;

import org.moskito.control.connectors.parsers.ParserHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Info provider for MySql.
 * Retrieves uptime info
 */
public class MySQLInfoProvider implements InfoProvider {

    /**
     * Query to get uptime
     */
    private static final String UPTIME_QUERY = "SHOW GLOBAL STATUS LIKE 'Uptime'";

    @Override
    public Map<String, String> getInfo(Connection connection, int timeout) throws SQLException {

        Map<String, String> info = new HashMap<>();

        ResultSet rs = connection.prepareStatement(UPTIME_QUERY).executeQuery();
        rs.next();

        long uptime = Long.valueOf((String) rs.getObject(2));

        info.put("Uptime",
                String.valueOf( uptime )
        );
        info.put("Uphours",
                String.valueOf( uptime / 3600 )
        );
        info.put("Updays",
                String.valueOf( uptime / 3600 / 24 )
        );

        return info;

    }

}
