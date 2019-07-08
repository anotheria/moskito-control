package org.moskito.control.data;

import org.junit.Test;
import org.moskito.control.data.thresholds.DataThreshold;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DataRepositoryTest {


//full config
//		"full DOWN 10 GREEN", "full UP 10 YELLOW", "full UP 15 ORANGE", "full UP 20 RED", "full UP 25 PURPLE",
//
// short config
//		"short DOWN 10 YELLOW", "short UP 10 ORANGE", "short UP 15 RED",
//
// ORANG instead ORANGE
//		"wrong DOWN 10 GREEN", "wrong UP 10 YELLOW", "wrong UP 15 ORANG", "wrong UP 20 RED", "wrong UP 25 PURPLE",
//
// short config with specific name
//		"named DOWN 10 GREEN", "named UP 10 ORANGE name4.T"

	@Test
	public void testDataThresholdsConfig() {
		DataRepository repository = DataRepository.getInstance();
		Map<String, DataThreshold> thresholds = new HashMap<>();
		for (DataThreshold threshold : repository.getThresholds()) {
			thresholds.put(threshold.getConfig().getGuardedVariableName(), threshold);
		}

		assertEquals(5, thresholds.get("full").getConfig().getGuards().size());
		assertEquals(3, thresholds.get("short").getConfig().getGuards().size());
		assertEquals(4, thresholds.get("wrong").getConfig().getGuards().size());
		assertEquals(2, thresholds.get("named").getConfig().getGuards().size());
	}

	@Test
	public void testUpdateforThreshold() {
		DataRepository repository = DataRepository.getInstance();
		Map<String, DataThreshold> thresholds = new HashMap<>();
		for (DataThreshold threshold : repository.getThresholds()) {
			thresholds.put(threshold.getConfig().getGuardedVariableName(), threshold);
		}


		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("full", "1");
		repository.update(dataMap);

		assertEquals("1", thresholds.get("full").getValue());
		assertEquals("none yet", thresholds.get("short").getValue());
		assertEquals("none yet", thresholds.get("wrong").getValue());
		assertEquals("none yet", thresholds.get("named").getValue());


		dataMap.put("full", "11");
		dataMap.put("wrong", "21");
		dataMap.put("named", "31");
		repository.update(dataMap);

		assertEquals("11", thresholds.get("full").getValue());
		assertEquals("none yet", thresholds.get("short").getValue());
		assertEquals("21", thresholds.get("wrong").getValue());
		assertEquals("31", thresholds.get("named").getValue());


		dataMap.put("short", "12");
		dataMap.put("other", "9");
		repository.update(dataMap);

		assertEquals("11", thresholds.get("full").getValue());
		assertEquals("12", thresholds.get("short").getValue());
		assertEquals("21", thresholds.get("wrong").getValue());
		assertEquals("31", thresholds.get("named").getValue());
	}
}

