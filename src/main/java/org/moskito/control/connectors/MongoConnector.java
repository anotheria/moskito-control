package org.moskito.control.connectors;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoTimeoutException;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.moskito.control.connectors.response.ConnectorAccumulatorResponse;
import org.moskito.control.connectors.response.ConnectorAccumulatorsNamesResponse;
import org.moskito.control.connectors.response.ConnectorStatusResponse;
import org.moskito.control.connectors.response.ConnectorThresholdsResponse;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.status.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Basic implementation of the Mongo connector.
 *
 * @author dzhmud
 * @since 14.04.2017 8:26 AM
 */
public class MongoConnector implements Connector {

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(MongoConnector.class);

    /**
     * Timeout for mongo operations that does not have timeout by default.
     * Connector should not wait 'forever'.
     */
    private static final int TIMEOUT =  10000;

    /**
     * Target JDBC url.
     */
    private String location;

    @Override
    public void configure(String location) {
        this.location = location;
    }

    @Override
    public ConnectorStatusResponse getNewStatus() {
        Status status = null;
        MongoClient mongoClient = null;
        try {
            final MongoClientURI connectionString = getMongoClientURI();
            final String dbName = connectionString.getDatabase() == null ? "test" : connectionString.getDatabase();
            mongoClient = new MongoClient(connectionString);
            //check if mongo URI is connectible at all
            boolean connected = false;
            try {
                mongoClient.isLocked();
                connected = true;
            } catch (MongoTimeoutException e) {
                log.warn("Failed to connect to mongo!", e);
                status = new Status(HealthColor.PURPLE, e.getMessage());
            }
            //try to gather some generic info
            if (connected) try {
                MongoDatabase db = mongoClient.getDatabase(dbName);

                Document serverStatus = db.runCommand(new Document("serverStatus", 1));
                String serverInfo = "host: " + serverStatus.get("host");
                serverInfo += "\nmongo version: " + serverStatus.get("version");
                serverInfo += "\nconnections: " + serverStatus.get("connections", Document.class).toJson();
                serverInfo += "\nmemory: " + serverStatus.get("mem", Document.class).toJson();
                status = new Status(HealthColor.GREEN, serverInfo);
            } catch (Throwable e) {
                log.warn("Failed to fetch mongoDb status!", e);
                status = new Status(HealthColor.RED, e.getMessage());
            }
        } catch (Throwable e) {
            log.warn("Check connection URI!", e);
            status = new Status(HealthColor.PURPLE, e.getMessage());
        } finally {
            if (mongoClient != null)
                try {
                    mongoClient.close();
                } catch (Throwable e) {
                    log.error("Failed to close mongo connection!", e);
                }
        }
        return new ConnectorStatusResponse(status);
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

    /**
     * Creates MongoClientURI from configured location. If needed, updates MongoClientOptions with timeouts.
     * If timeouts configured via location string, they will remain.
     * @return MongoClientURI created from configured location.
     */
    private MongoClientURI getMongoClientURI() {
        if (location == null)
            throw new IllegalArgumentException("Provided location is empty!");
        MongoClientURI connectionString = new MongoClientURI(location);
        MongoClientOptions options = connectionString.getOptions();
        if (options.getConnectTimeout() * options.getSocketTimeout() * options.getServerSelectionTimeout() == 0) {
            MongoClientOptions.Builder builder = new MongoClientOptions.Builder(options);
            builder.connectTimeout(selectTimeout(options.getConnectTimeout(), TIMEOUT));
            builder.socketTimeout(selectTimeout(options.getSocketTimeout(), TIMEOUT));
            builder.serverSelectionTimeout(selectTimeout(options.getServerSelectionTimeout(), TIMEOUT));
            builder.maxWaitTime(selectTimeout(options.getMaxWaitTime(), TIMEOUT));
            connectionString = new MongoClientURI(location, builder);
        }
        return connectionString;
    }

    private static int selectTimeout(int currentValue, int defaultValue) {
        return currentValue > 0 ? currentValue : defaultValue;
    }

}
