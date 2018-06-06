package org.moskito.control.data.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.moskito.control.data.DataRetriever;

import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 04.06.18 16:49
 */
public class TestMoSKitoRetriever implements DataRetriever{

	private String baseUrl;
	private List<MoSKitoValueMapping> mappings = new LinkedList<>();

	public TestMoSKitoRetriever(String aBaseUrl, MoSKitoValueMapping ... mappings){
		baseUrl = aBaseUrl;
		for (MoSKitoValueMapping m : mappings){
			this.mappings.add(m);
		}
	}

	@Override
	public Map<String, String> retrieveData() {

		HashMap<String, String> variableNames = new HashMap<>();
		HashMap<String,String> data = new HashMap<>();

		Client client = Client.create();
		WebResource webResource = client.resource(baseUrl+"/value");
		JsonArray arr = new JsonArray();
		for (MoSKitoValueMapping m : mappings) {
			JsonObject mapping = new JsonObject();
			mapping.addProperty("producerName", m.getProducerName());
			mapping.addProperty("statName", m.getStatName());
			mapping.addProperty("valueName", m.getValueName());
			mapping.addProperty("intervalName", m.getIntervalName());
			mapping.addProperty("timeUnit", m.getTimeUnitName());
			arr.add(mapping);
			variableNames.put(m.getMoskitoId(), m.getTargetVariableName());
		}
		ClientResponse response = webResource.
				accept(MediaType.APPLICATION_JSON).
				type(MediaType.APPLICATION_JSON).
				post(ClientResponse.class, arr.toString());

		String content = response.getEntity(String.class);

		JsonParser parser = new JsonParser();
		JsonObject root = (JsonObject)parser.parse(content);

		JsonPrimitive success = root.getAsJsonPrimitive("success");
		if (!success.getAsBoolean()){
			System.out.println("Not successful response");
			return Collections.emptyMap();
		}

		JsonArray values = root.getAsJsonObject("results").getAsJsonArray("values");
		for (JsonElement value : values){
			JsonObject v = (JsonObject)value;
			if (!v.getAsJsonPrimitive("success").getAsBoolean()){
				continue;
			}
			JsonObject valueRequest = v.getAsJsonObject("request");
			String targetValue = v.getAsJsonPrimitive("value").getAsString();

			String producerName = valueRequest.get("producerName").getAsString();
			String statName = valueRequest.get("statName").getAsString();
			String valueName = valueRequest.get("valueName").getAsString();
			String intervalName = valueRequest.get("intervalName").getAsString();
			String timeUnit = valueRequest.get("timeUnit").getAsString();
			String variableName = variableNames.get(MoSKitoValueMapping.getMoskitoId(producerName, statName, valueName, intervalName, timeUnit));
			data.put(variableName, targetValue);

		}

		return data;
	}
}
