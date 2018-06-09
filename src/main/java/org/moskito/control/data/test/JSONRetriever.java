package org.moskito.control.data.test;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.apache.http.HttpResponse;
import org.moskito.control.connectors.httputils.HttpHelper;
import org.moskito.control.data.DataRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 09.06.18 00:10
 */
public class JSONRetriever implements DataRetriever{

	private static Logger log = LoggerFactory.getLogger(JSONRetriever.class);

	private String url;
	private List<JSONValueMapping> mappings;

	public JSONRetriever() {
	}

	@Override
	public Map<String, String> retrieveData() {


		try {
			HttpResponse response = HttpHelper.getHttpResponse(url);
			if (response.getStatusLine().getStatusCode() != 200) {
				log.warn("Couldn't retrieve " + url + ", " + response.getStatusLine());
				return Collections.emptyMap();
			}
			HashMap<String, String> ret = new HashMap<>();
			String json = HttpHelper.getResponseContent(response);
			ReadContext ctx = JsonPath.parse(json);

			for (JSONValueMapping mapping : mappings){
				try{
					String value = ""+ctx.read(mapping.getJsonPathExpression());
					ret.put(mapping.getVariableName(), value);
				}catch(Exception any){
					log.error("Can't parse json expression "+mapping.getJsonPathExpression()+" for "+mapping.getVariableName(), any);
				}
			}

			return ret;

		}catch(IOException e){
			log.warn("Couldn't retrieve url ", e);
			return Collections.emptyMap();
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<JSONValueMapping> getMappings() {
		return mappings;
	}

	public void setMappings(List<JSONValueMapping> mappings) {
		this.mappings = mappings;
	}
}
