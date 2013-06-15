package org.moskito.control.connectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.Status;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 28.05.13 21:01
 */
public class HttpConnector implements Connector {

	public static final String FILTER_MAPPING = "/moskito-control-agent/status";

	private String location;

	private static Logger log = Logger.getLogger(HttpConnector.class);

	@Override
	public void configure(String location) {
		this.location = location;
	}

	@Override
	public ConnectorResponse getNewStatus() {
		log.debug("Trying to call "+location);

		String targetUrl = location;
		if (targetUrl.endsWith("/"))
			targetUrl+=FILTER_MAPPING.substring(1);
		else
			targetUrl+=FILTER_MAPPING;

		if (!targetUrl.startsWith("http")){
			targetUrl = "http://"+targetUrl;
		}

		log.debug("URL to Call "+targetUrl);

		String resultAsString = null;

		try {
			URLConnection urlC = new URL(targetUrl).openConnection();
			InputStream in = urlC.getInputStream();
			InputStreamReader reader = new InputStreamReader(in);
			char[] result = new char[in.available()];
			reader.read(result);

			resultAsString = new String(result);
			log.debug("RESULT is "+resultAsString);
		} catch (IOException e) {
			new ConnectorResponse(new Status(HealthColor.PURPLE, "Connection Error: "+e.getMessage()));
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		HashMap<String,String> parsed = (HashMap<String,String>)gson.fromJson(resultAsString, HashMap.class);
		ConnectorResponseParser parser = ConnectorResponseParsers.getParser(parsed);
		ConnectorResponse myResponse = parser.parseResponse(parsed);
		return myResponse;
	}

}
