package org.moskito.control.connectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.anotheria.util.StringUtils;
import org.moskito.control.connectors.httputils.HttpHelper;
import org.moskito.control.connectors.parsers.ConnectorResponseParser;
import org.moskito.control.connectors.parsers.ConnectorResponseParsers;
import org.moskito.control.connectors.response.ConnectorAccumulatorResponse;
import org.moskito.control.connectors.response.ConnectorAccumulatorsNamesResponse;
import org.moskito.control.connectors.response.ConnectorStatusResponse;
import org.moskito.control.connectors.response.ConnectorThresholdsResponse;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.status.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 * A basic implementation of the http connector that connects to moskito-control-agent http-filter.
 *
 * @author lrosenberg
 * @since 28.05.13 21:01
 */
public class HttpConnector implements Connector {

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

    /**
	 * Target applications url.
	 */
	private String location;

	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(HttpConnector.class);

	@Override
	public void configure(String aLocation) {
		this.location = aLocation;
	}

	private void debugSaveContentToFile(String name, String content){
		if (content==null)
			return;
		name = StringUtils.replace(name, ':', '_');
		name = StringUtils.replace(name, '/', '_');
		name = StringUtils.replace(name, '?', '_');
		name = StringUtils.replace(name, '&', '_');
		try{
			FileOutputStream fOut = new FileOutputStream(name);
			fOut.write(content.getBytes("UTF-8"));
			fOut.close();
		}catch(Exception e){
			e.printStackTrace();
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
			return myResponse;
		}catch(IOException e){
			return new ConnectorStatusResponse(new Status(HealthColor.PURPLE, "Connection Error: "+e.getMessage()));
		}
	}

    @Override
    public ConnectorThresholdsResponse getThresholds() throws IOException {
        HashMap<String,String> data = getTargetData(OP_THRESHOLDS);
        if (data == null) {
            return null;
        }
        ConnectorResponseParser parser = ConnectorResponseParsers.getParser(data);
        ConnectorThresholdsResponse response = parser.parseThresholdsResponse(data);
        return response;
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

}
