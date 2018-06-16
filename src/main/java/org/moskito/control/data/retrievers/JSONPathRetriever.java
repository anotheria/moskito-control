package org.moskito.control.data.retrievers;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.apache.http.HttpResponse;
import org.moskito.control.connectors.httputils.HttpHelper;
import org.moskito.control.config.datarepository.VariableMapping;
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
public class JSONPathRetriever implements DataRetriever{

	private static Logger log = LoggerFactory.getLogger(JSONPathRetriever.class);

	private String url;
	private List<VariableMapping> mappings;

	public JSONPathRetriever() {
	}

	@Override
	public void configure(String configurationParameter, List<VariableMapping> mappings) {
		url = configurationParameter;
		this.mappings = mappings;
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

			for (VariableMapping mapping : mappings){
				try{
					String value = ""+ctx.read(mapping.getExpression());
					ret.put(mapping.getVariableName(), value);
				}catch(Exception any){
					log.error("Can't parse json expression "+mapping.getExpression()+" for "+mapping.getVariableName(), any);
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

	public List<VariableMapping> getMappings() {
		return mappings;
	}

	public void setMappings(List<VariableMapping> mappings) {
		this.mappings = mappings;
	}
}
