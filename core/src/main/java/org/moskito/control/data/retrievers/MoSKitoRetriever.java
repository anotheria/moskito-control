package org.moskito.control.data.retrievers;

import com.google.gson.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.anotheria.util.StringUtils;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.moskito.control.config.datarepository.VariableMapping;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * This retriever handles retrieval of statistic values from a remote moskito installation via moskito-rest-api (since 2.8.8).
 *
 * @author lrosenberg
 * @since 04.06.18 16:49
 */
public class MoSKitoRetriever implements DataRetriever{

	/**
	 * Url of the moskito instance.
	 */
	private String baseUrl;
	private List<MoSKitoValueMapping> mappings = new LinkedList<>();

	public MoSKitoRetriever(){
	}

	@Override
	public void configure(String configurationParameter, List<VariableMapping> mappingsParameter) {
		baseUrl = configurationParameter;
		mappings = new LinkedList<>();
		for (VariableMapping vm : mappingsParameter){
		 	MoSKitoValueMapping mvm = new MoSKitoValueMapping();
		 	mvm.setTargetVariableName(vm.getVariableName());
		 	String tokens[] = StringUtils.tokenize(vm.getExpression(), '.');
			mvm.setProducerName(tokens[0]);
			mvm.setStatName(tokens[1]);
			mvm.setValueName(tokens[2]);
			mvm.setIntervalName(tokens[3]);
			mvm.setTimeUnitName(tokens[4]);
			mappings.add(mvm);

		}
	}

	@Override
	public Map<String, String> retrieveData() {

		HashMap<String, String> variableNames = new HashMap<>();
		HashMap<String,String> data = new HashMap<>();

		Client client = new JerseyClientBuilder()
				.readTimeout(60_000, TimeUnit.MILLISECONDS)
				.connectTimeout(60_000, TimeUnit.MILLISECONDS)
				.build();

		WebTarget webTarget = client.target(baseUrl+"/value");

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
		Response response = null;
		Entity<String> entity = Entity.entity(arr.toString(), MediaType.APPLICATION_JSON_TYPE);
		response = webTarget.request().
				accept(MediaType.APPLICATION_JSON).
				post(entity);


		String content = response.readEntity(String.class);
		JsonParser parser = new JsonParser();
		JsonObject root = (JsonObject) parser.parse(content);

		JsonPrimitive success = root.getAsJsonPrimitive("success");
		if (!success.getAsBoolean()){
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

	@Override
	public String toString() {
		return "MoSKitoRetriever{" +
				"baseUrl='" + baseUrl + '\'' +
				", mappings=" + mappings +
				'}';
	}
}
