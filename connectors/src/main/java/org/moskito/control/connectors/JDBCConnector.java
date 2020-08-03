package org.moskito.control.connectors;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.moskito.control.connectors.jdbc.InfoProviderManager;
import org.moskito.control.connectors.parsers.ParserHelper;
import org.moskito.control.connectors.response.*;
import org.moskito.control.common.HealthColor;
import org.moskito.control.common.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Basic implementation of the JDBC connector. Configured with the standard JDBC url.
 *
 * @author dzhmud
 * @since 12.04.2017 3:16 PM
 */
public class JDBCConnector extends AbstractConnector {

    /**
     * SQL query executed after successful connect.
     */
    private static final String QUERY = "select version();";
    /**
     * Timeout in seconds for both login/query.
     */
    private static final int TIMEOUT = 5;
    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(JDBCConnector.class);

    /**
     * Target JDBC url.
     */
    private String location;
    /**
     * Target DB credentials.
     */
    private String credentials;

    static {
        DriverManager.setLoginTimeout(TIMEOUT);
        String[] drivers = {"org.postgresql.Driver", "com.mysql.jdbc.Driver"};
        for (String clazz : drivers) {
            try {
                Class.forName(clazz);
            } catch (ClassNotFoundException e) {
                log.warn("Tried to load JDBC driver '" + clazz + "' but failed!", e);
            }
        }
    }

    private String getDbType(){
        return location.substring(
            location.indexOf(':') + 1,
                location.indexOf(':', location.indexOf(':') + 1)
        );
    }

    @Override
    public void configure(String componentName, String location, String credentials) {
        this.location = location;
        this.credentials = credentials;
    }

    private Connection getConnection() throws SQLException{

        UsernamePasswordCredentials dbCredentials = ParserHelper.getCredentials(credentials);

        if (dbCredentials == null)
            return DriverManager.getConnection(location);
        else
            return DriverManager.getConnection(location, dbCredentials.getUserName(), dbCredentials.getPassword());

    }

    @Override
    public ConnectorStatusResponse getNewStatus() {
        Status status = null;
        Connection connection = null;
        try {
            log.debug("checking " + location);
            connection = getConnection();
        } catch (SQLException e) {
            status = new Status(HealthColor.PURPLE, getMessage(e));
        }
        if (connection != null) {
            PreparedStatement st = null;
            ResultSet rs = null;
            try {
                st = connection.prepareStatement(QUERY);
                st.setQueryTimeout(TIMEOUT);
                rs = st.executeQuery();
                if (rs.next()) {
                    status = new Status(HealthColor.GREEN, rs.getString(1));
                } else {
                    status = new Status(HealthColor.RED, "Failed to query server version.");
                    log.warn("'select version()' query returned nothing for location " + location);
                }
            } catch (SQLException e) {
                status = new Status(HealthColor.RED, getMessage(e));
                log.warn("health check failed:", e);
            } finally {
                try {
                    if(st != null) st.close();
                    if(rs != null) rs.close();
                    connection.close();
                } catch (SQLException e) {
                    log.error("Failed to close connection:", e);
                }
            }
        }
        return new ConnectorStatusResponse(status);
    }

    private static String getMessage(SQLException e) {
        return e == null ? "null" : e.getMessage() + ". SQLState: " + e.getSQLState();
    }

    @Override
    public ConnectorThresholdsResponse getThresholds() {
        return new ConnectorThresholdsResponse();
    }

    @Override
    public ConnectorAccumulatorResponse getAccumulators(List<String> accumulatorNames) {
        return new ConnectorAccumulatorResponse();
    }

    @Override
    public ConnectorAccumulatorsNamesResponse getAccumulatorsNames() throws IOException {
        return new ConnectorAccumulatorsNamesResponse();
    }

    @Override
    public boolean supportsInfo(){
        return true;
    }

    @Override
    public ConnectorInformationResponse getInfo() {

        ConnectorInformationResponse response =
                new ConnectorInformationResponse();

        try {

            Connection connection = getConnection();

            if (connection != null) {

                Map<String, String> info = InfoProviderManager.getFor(getDbType()).getInfo(connection, TIMEOUT);

                DatabaseMetaData metaData = connection.getMetaData();
                info.put("JDBC Driver Version", metaData.getDriverName() + " " + metaData.getDriverVersion());
                info.put("DB Name", metaData.getDatabaseProductName());
                info.put("DB Version", metaData.getDatabaseProductVersion());

                response.setInfo(info);
                return response;

            }

        } catch (SQLException ignored) {}

        response.setInfo(new HashMap<>());
        return response;

    }

}
