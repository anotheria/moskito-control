package org.moskito.control.connectors;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import net.anotheria.util.StringUtils;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.bson.Document;
import org.moskito.control.connectors.parsers.ParserHelper;
import org.moskito.control.connectors.response.*;
import org.moskito.control.common.HealthColor;
import org.moskito.control.common.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Basic implementation of the Mongo connector.
 *
 * @author dzhmud
 * @since 14.04.2017 8:26 AM
 */
public class MongoConnector extends AbstractConnector {

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(MongoConnector.class);

    /**
     * Timeout value(in ms) for mongo operations that does not have timeout by default.
     * Connector should not wait 'forever'.
     */
    private static final int TIMEOUT =  10000;

    /**
     * Test command executed if location is correct. Ping should work for all users, I guess.
     */
    private static Document TEST_COMMAND = new Document("ping", 1);
    private static Document SERVER_STATUS = new Document("serverStatus", 1);

    /**
     * Target MongoDB url.
     */
    private String location;

    /**
     * Target DB credentials in the same form as they appear in mongo connectionString.
     */
    private String credentials;

    @Override
    public void configure(String componentName, String aLocation, String aCredentials) {
        this.credentials = parseMongoCredentials(aCredentials);
        this.location = getLocationWithCredentials(aLocation, this.credentials);
    }

    /**
     * Executes given command making new connection
     * to database, specified by location property of
     * this connector.
     * @param command command
     * @return response document
     * @throws MongoException thrown by mongo driver
     * @throws IllegalArgumentException thrown by mongo driver on connection initialization fail
     */
    private Document executeCommand(Document command) throws MongoException, IllegalArgumentException{

        final MongoClient mongoClient;

        final MongoClientURI connectionString = getMongoClientURI();
        final String dbName = connectionString.getDatabase() == null ? "test" : connectionString.getDatabase();
        //try to get cached MongoClient
        mongoClient = MongoClient.class.cast(Mongo.Holder.singleton().connect(connectionString));

        //check if mongo URI is connectible at all and execute test command
        mongoClient.isLocked();
        MongoDatabase db = mongoClient.getDatabase(dbName);

        return db.runCommand(command);

    }

    @Override
    public ConnectorStatusResponse getNewStatus() {

        Status status;

        try {

            Document ping = executeCommand(TEST_COMMAND);

            if (isCommandOk(ping)) {
                status = new Status(HealthColor.GREEN, "");
            } else {
                status = new Status(HealthColor.RED, getFailureMessage(ping));
            }

        } catch (IllegalArgumentException e) {
            log.warn("Check connection URI!", e);
            status = new Status(HealthColor.PURPLE, getFailureMessage(e));
        }
        catch (MongoException e) {
            log.warn("Failed to connect to mongo instance!", e);
            status = new Status(HealthColor.PURPLE, getFailureMessage(e));
        }

        return new ConnectorStatusResponse(status);

    }

    /**
     * Check if mongo response has key "ok" and it's value.
     * @param document mongo response to test command.
     * @return {@code true} if document has key "ok" and it's value is boolean true
     * or numeric 1, {@code false} otherwise.
     */
    private static boolean isCommandOk(final Document document) {
        boolean result = false;
        if (document != null) {
            final Object okValue = document.get("ok");
            if (okValue instanceof Boolean) {
                result = Boolean.class.cast(okValue);
            } else if (okValue instanceof Number) {
                result = ((Number) okValue).intValue() == 1;
            }
        }
        return result;
    }

    /** If mongo returned document with "ok:0", try to get error message from it. */
    private String getFailureMessage(Document document) {
        String message = document.getString("errmsg");
        if (StringUtils.isEmpty(message)) {
            message = document.toJson();
        }
        return stripCredentials(message);
    }

    /** Get message from exception caught and cut out credentials from it. */
    private String getFailureMessage(Throwable e) {
        return stripCredentials(e.getMessage());
    }

    /** Method tries to cut credentials from given text. */
    private String stripCredentials(String text) {
        if (!StringUtils.isEmpty(text) && !StringUtils.isEmpty(credentials)) {
            while(text.contains(credentials)) {
                text = text.replace(credentials, "[username:password@]");
            }
        }
        return text;
    }

    /** prepare credentials for later use in connection string */
    private static String parseMongoCredentials(String credentials) {
        UsernamePasswordCredentials creds = ParserHelper.getCredentials(credentials);
        return (creds == null) ? null : creds.getUserName() + ":" + creds.getPassword() + "@";
    }

    /**
     * Configuration should have separate field for credentials, insert them in the mongo connection string.
     * mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]
     */
    private static String getLocationWithCredentials(String location, String credentials) {
        final String prefix = "mongodb://";
        String result = location;
        if (credentials != null && location != null && location.startsWith(prefix)) {
            result = prefix + credentials + location.substring(prefix.length());
        }
        return result;
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

    public boolean supportsInfo(){
        return true;
    }

    @Override
    public ConnectorInformationResponse getInfo() {

        Map<String, String> info = new HashMap<>();
        ConnectorInformationResponse response =
                new ConnectorInformationResponse();

        try{

            Document serverStatus = executeCommand(SERVER_STATUS);

            info.put("Version", serverStatus.get("version").toString());

            long uptime = ((Number) serverStatus.get("uptime")).longValue();
            info.put("Uptime", String.valueOf(uptime));
            info.put("Uphours",
                    String.valueOf(uptime / 3600)
            );
            info.put("Updays",
                    String.valueOf(uptime / (3600*24))
            );

            Object storageEngineDocument = serverStatus.get("storageEngine");

            if(storageEngineDocument != null)
                info.put("Storage Engine", ((Document) storageEngineDocument).get("name").toString());

        }catch(IllegalArgumentException | MongoException ignored){
            // Empty map be returned
        }

        response.setInfo(info);

        return response;

    }

}
