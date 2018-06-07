package org.moskito.control.data.processors;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 07.06.18 18:00
 */
public class CalculatePercentageDeviationProcessorTest {
	@Test public void testIdenticalValues(){
		HashMap<String,String> data = new HashMap<>();
		data.put("a", "10");
		data.put("b", "10");
		CalculatePercentageDeviationProcessor processor = new CalculatePercentageDeviationProcessor();
		processor.configure("c", "a,b");
		Map<String,String> result = processor.process(data);
		String c = result.get("c");

		assertNotNull(c);
		assertEquals("0.0", c);

	}

	@Test public void testGreaterValue(){
		HashMap<String,String> data = new HashMap<>();
		data.put("a", "15");
		data.put("b", "10");
		CalculatePercentageDeviationProcessor processor = new CalculatePercentageDeviationProcessor();
		processor.configure("c", "a,b");
		Map<String,String> result = processor.process(data);
		String c = result.get("c");

		assertNotNull(c);
		assertEquals("50.0", c);

	}

	@Test public void testGreaterValueWithComma(){
		HashMap<String,String> data = new HashMap<>();
		data.put("a", "13");
		data.put("b", "11");
		CalculatePercentageDeviationProcessor processor = new CalculatePercentageDeviationProcessor();
		processor.configure("c", "a,b");
		Map<String,String> result = processor.process(data);
		String c = result.get("c");

		assertNotNull(c);
		assertEquals("18.2", c);

	}

	@Test public void testSmallerValue(){
		HashMap<String,String> data = new HashMap<>();
		data.put("a", "10");
		data.put("b", "13");
		CalculatePercentageDeviationProcessor processor = new CalculatePercentageDeviationProcessor();
		processor.configure("c", "a,b");
		Map<String,String> result = processor.process(data);
		String c = result.get("c");

		assertNotNull(c);
		assertEquals("-23.0", c);

	}

}
