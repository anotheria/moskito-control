package org.moskito.controlagent.endpoints.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import net.anotheria.moskito.core.threshold.ThresholdStatus;
import org.junit.Test;
import org.moskito.control.common.HealthColor;
import org.moskito.control.connectors.parsers.ConnectorResponseParser;
import org.moskito.control.connectors.parsers.ConnectorResponseParsers;
import org.moskito.control.connectors.response.ConnectorStatusResponse;
import org.moskito.control.connectors.response.ConnectorThresholdsResponse;
import org.moskito.controlagent.data.status.StatusHolder;
import org.moskito.controlagent.data.status.ThresholdInfo;
import org.moskito.controlagent.data.threshold.ThresholdDataItem;
import org.moskito.controlagent.endpoints.ReplyWrapper;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Proves that what {@link MoskitoControlActuatorEndpoint} emits stays wire-compatible with the
 * MoSKito Control collector.
 * <p>
 * The production pipeline is asymmetric: the actuator endpoint serializes with Jackson (Spring
 * Boot's mapper), while the control side ({@code HttpConnector}) always re-parses the body with
 * gson into a {@code HashMap} before handing it to the versioned parsers. This test reproduces
 * exactly that path - Jackson out, gson in, real {@link ConnectorResponseParsers} - so any
 * serializer drift (field naming, enum rendering, number typing) fails here rather than in the field.
 *
 * @author lrosenberg
 */
public class WireCompatibilityTest {

	private final ObjectMapper jackson = new ObjectMapper();
	private final Gson gson = new Gson();

	/** Serialize like the actuator endpoint (Jackson), re-parse like the connector (gson). */
	@SuppressWarnings("unchecked")
	private HashMap<String, Object> throughTheWire(Object payload) throws Exception {
		String json = jackson.writeValueAsString(new ReplyWrapper(payload));
		return gson.fromJson(json, HashMap.class);
	}

	@Test
	public void statusResponseIsWireCompatible() throws Exception {
		StatusHolder status = new StatusHolder();
		status.setStatus(ThresholdStatus.RED);
		status.setNowRunning(7);
		ThresholdInfo info = new ThresholdInfo();
		info.setThreshold("Memory");
		info.setValue("95");
		info.setMessage("almost full");
		status.addThresholdInfo(info);

		HashMap<String, Object> reply = throughTheWire(status);

		// Parser selection is driven by the protocolVersion in the envelope.
		ConnectorResponseParser parser = ConnectorResponseParsers.getParser(reply);
		ConnectorStatusResponse response = parser.parseStatusResponse(reply);

		assertEquals(HealthColor.RED, response.getStatus().getHealth());
		assertEquals(7, response.getNowRunningCount());
	}

	@Test
	public void thresholdsResponseIsWireCompatible() throws Exception {
		ThresholdDataItem item = new ThresholdDataItem();
		item.setName("Memory");
		item.setStatus(ThresholdStatus.YELLOW);
		item.setLastValue("80");
		item.setStatusChangeTimestamp(1234567890L);

		HashMap<String, Object> reply = throughTheWire(List.of(item));

		ConnectorResponseParser parser = ConnectorResponseParsers.getParser(reply);
		ConnectorThresholdsResponse response = parser.parseThresholdsResponse(reply);

		assertEquals(1, response.getItems().size());
		assertEquals("Memory", response.getItems().get(0).getName());
		assertEquals(HealthColor.YELLOW, response.getItems().get(0).getStatus());
		assertEquals(1234567890L, response.getItems().get(0).getStatusChangeTimestamp());
	}
}
