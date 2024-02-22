package org.moskito.control.connectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.anotheria.util.StringUtils;
import org.moskito.control.common.HealthColor;
import org.moskito.control.common.Status;
import org.moskito.control.config.ComponentConfig;
import org.moskito.control.connectors.httputils.HttpHelper;
import org.moskito.control.connectors.parsers.ConnectorResponseParser;
import org.moskito.control.connectors.parsers.ConnectorResponseParsers;
import org.moskito.control.connectors.response.ConnectorAccumulatorResponse;
import org.moskito.control.connectors.response.ConnectorAccumulatorsNamesResponse;
import org.moskito.control.connectors.response.ConnectorConfigResponse;
import org.moskito.control.connectors.response.ConnectorInformationResponse;
import org.moskito.control.connectors.response.ConnectorNowRunningResponse;
import org.moskito.control.connectors.response.ConnectorStatusResponse;
import org.moskito.control.connectors.response.ConnectorThresholdsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A basic implementation of the http connector that connects to moskito-control-agent http-filter.
 *
 * @author lrosenberg
 * @since 28.05.13 21:01
 */
public class HttpConnector extends AbstractConnector {

	/**
	 * Path to agent-filter.
	 */
	public static final String FILTER_MAPPING = "/moskito-control-agent/";

	/**
	 * Constant for the status request operation.
	 */
	public static final String OP_STATUS = "status";
	/**
	 * Constant for the accumulator data operation.
	 */
	public static final String OP_ACCUMULATOR = "accumulator";
    /**
     * Constant for the threshold list operation.
     */
    public static final String OP_THRESHOLDS = "thresholds";
    /**
     * Constant for the accumulators names list operation.
     */
    private static final String OP_ACCUMULATORS = "accumulators";

    private static final String OP_INFO = "info";
    private static final String OP_CONFIG = "config";
    private static final String OP_NOWRUNNING = "nowrunning";

    /**
	 * Target applications url.
	 */
	private String location;

	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(HttpConnector.class);

	private void debugSaveContentToFile(String name, String content){
		if (content==null)
			return;
		name = StringUtils.replace(name, ':', '_');
		name = StringUtils.replace(name, '/', '_');
		name = StringUtils.replace(name, '?', '_');
		name = StringUtils.replace(name, '&', '_');
		FileOutputStream fOut = null;
		try{
			fOut = new FileOutputStream(name);
			fOut.write(content.getBytes("UTF-8"));
		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
			if(fOut != null) try {
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private HashMap<String,String> getTargetData(String operation) throws IOException {

		String targetUrl = location;
		if (targetUrl.endsWith("/"))
			targetUrl+=FILTER_MAPPING.substring(1);
		else
			targetUrl+=FILTER_MAPPING;
		targetUrl += operation;
		if (!targetUrl.startsWith("http")){
			targetUrl = "http://"+targetUrl;
		}

		log.debug("URL to Call "+targetUrl);

		String content = HttpHelper.getURLContent(targetUrl);
		//debugSaveContentToFile(targetUrl, content);
		//log.debug("RESULT for "+targetUrl+" is "+content);


		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try{
			HashMap<String,String> parsed = (HashMap<String,String>)gson.fromJson(content, HashMap.class);
			return parsed;
		}catch(JsonSyntaxException e){
			log.error("Can't parse status reply: "+content);
			throw new ConnectorException("Can't parse reply", e);
		}
	}

	@Override
	public ConnectorStatusResponse getNewStatus() {
		try{
			HashMap<String,String> data = getTargetData(OP_STATUS);
			ConnectorResponseParser parser = ConnectorResponseParsers.getParser(data);
			ConnectorStatusResponse myResponse = parser.parseStatusResponse(data);
			if(myResponse.getStatus().getHealth().equals(HealthColor.PURPLE)){
			    for(Map.Entry<String, String> entry: data.entrySet()){
                    System.out.println(entry.getKey()+"  "+entry.getValue());
                }
            }
			return myResponse;
		}catch(IOException e){
		    e.printStackTrace();
            System.out.println(e.getMessage());
			return new ConnectorStatusResponse(new Status(HealthColor.PURPLE, "Connection Error: "+e.getMessage()));
		}
	}

    @Override
    public ConnectorThresholdsResponse getThresholds(){
		try {
			HashMap<String, String> data = getTargetData(OP_THRESHOLDS);
			if (data == null) {
				return null;
			}
			ConnectorResponseParser parser = ConnectorResponseParsers.getParser(data);
			ConnectorThresholdsResponse response = parser.parseThresholdsResponse(data);
			return response;
		}catch(IOException e){
			throw new ConnectorException("Couldn't get thresholds data", e );
		}
    }

    @Override
	public ConnectorAccumulatorResponse getAccumulators(List<String> accumulatorNames) {
		String operation = OP_ACCUMULATOR;
		for (String a : accumulatorNames){
			try {
				operation += "/"+ URLEncoder.encode(a, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new AssertionError("UTF-8 is not supported encoding, the world must have been broken apart - "+e.getMessage());
			}
		}
		try {
			HashMap<String,String> data = getTargetData(operation);
			if (data==null){
				return null;
			}
			ConnectorResponseParser parser = ConnectorResponseParsers.getParser(data);
			ConnectorAccumulatorResponse response = parser.parseAccumulatorResponse(data);
			return response;
		} catch(IOException e) {
			throw new RuntimeException("Not yet handled", e);
		}
	}

    @Override
    public ConnectorAccumulatorsNamesResponse getAccumulatorsNames() throws IOException {
        HashMap<String,String> data = getTargetData(OP_ACCUMULATORS);
        if (data == null) {
            return null;
        }
        ConnectorResponseParser parser = ConnectorResponseParsers.getParser(data);
        ConnectorAccumulatorsNamesResponse response = parser.parseAccumulatorsNamesResponse(data);
        return response;
    }

	@Override
	public boolean supportsInfo() {
		return true;
	}

	@Override
	public boolean supportsThresholds() {
		return true;
	}

	@Override
	public boolean supportsAccumulators() {
		return true;
	}

	/**
	 * Returns information about monitored app
	 * and its environment.
	 *
	 * Map has following fields:
	 * 	javaVersion  - version of java, where monitored app is launched
	 * 	startCommand - command, that launched monitored app
	 * 	machineName  - name of machine, where monitored app is launched.
	 * 	uptime       - monitored app uptime
	 *
	 * @return map with monitored app information
	 */
	@Override
	public ConnectorInformationResponse getInfo() {
		try {
			Map httpResponseMap = getTargetData(OP_INFO);
			ConnectorResponseParser parser = ConnectorResponseParsers.getParser(httpResponseMap);
			return parser.parseInformationResponse(httpResponseMap);
		} catch (IOException | ClassCastException e) {
			throw new ConnectorException("Couldn't obtain info from server at " + location);
		}
	}

	@Override
	public boolean supportsConfig() {
		return true;
	}

    @Override
    public ConnectorConfigResponse getConfig() {
        try {
            Map<String, String> data = getTargetData(OP_CONFIG);
            if (data == null) {
                return null;
            }

            ConnectorResponseParser parser = ConnectorResponseParsers.getParser(data);
            return parser.parseConfigResponse(data);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ConnectorException("Couldn't get config data", e);
        }
    }

	@Override
	public boolean supportsNowRunning() {
		return true;
	}

	@Override
	public ConnectorNowRunningResponse getNowRunning() {
		try {
			Map<String, String> data = getTargetData(OP_NOWRUNNING);
			if (data==null)
				return new ConnectorNowRunningResponse();
			ConnectorResponseParser parser = ConnectorResponseParsers.getParser(data);
			return parser.parseNowRunningResponse(data);

		}catch(IOException e){
			throw new ConnectorException("Can't retrieve data", e);
		}
	}

	@Override
	public void configure(ComponentConfig connectorConfig) {
		location = connectorConfig.getLocation();
	}
}


