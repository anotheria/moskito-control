package org.moskito.control.connectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.Status;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
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

	public static final String OP_STATUS = "status";
	public static final String OP_ACCUMULATOR = "accumulator";
	public static final String OP_ACCUMULATORS = "accumulators";

	/**
	 * Target applications url.
	 */
	private String location;

	/**
	 * Logger.
	 */
	private static Logger log = Logger.getLogger(HttpConnector.class);

	@Override
	public void configure(String location) {
		this.location = location;
	}


	private HashMap<String,String> getTargetData(String operation) throws IOException{
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

		String resultAsString = null;

		URLConnection urlC = new URL(targetUrl).openConnection();
		InputStream in = urlC.getInputStream();
		InputStreamReader reader = new InputStreamReader(in);
		char[] result = new char[in.available()];
		reader.read(result);

		resultAsString = new String(result);
		log.debug("RESULT is "+resultAsString);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		HashMap<String,String> parsed = (HashMap<String,String>)gson.fromJson(resultAsString, HashMap.class);
		return parsed;
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
	public ConnectorAccumulatorResponse getAccumulators(List<String> accumulatorNames) {
		String operation = OP_ACCUMULATOR;
		for (String a : accumulatorNames){
			try {
				operation += "/"+ URLEncoder.encode(a, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new AssertionError("UTF-8 is not supported encoding, the world must have been broken apart", e);
			}
		}
		try{
			HashMap<String,String> data = getTargetData(operation);
			ConnectorResponseParser parser = ConnectorResponseParsers.getParser(data);
			ConnectorAccumulatorResponse response = parser.parseAccumulatorResponse(data);
			return response;
		}catch(IOException e){
			throw new RuntimeException("Not yet handled" ,e );
		}
	}


}
