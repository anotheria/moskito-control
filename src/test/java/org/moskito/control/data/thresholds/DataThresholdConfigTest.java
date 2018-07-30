package org.moskito.control.data.thresholds;

import org.junit.Test;
import org.moskito.control.core.HealthColor;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DataThresholdConfigTest {

	@Test
	public void testFullSingleLine() {
		List<String> lines = new LinkedList<>();
		lines.add("varName UP 10 GREEN anotherName");
		DataThresholdConfig config = new DataThresholdConfig();
		config.configure("varName", lines);

		assertEquals("varName", config.getGuardedVariableName());
		assertEquals("anotherName", config.getTargetVariableName());
		assertEquals(1, config.getGuards().size());
		assertEquals(GuardDirection.UP, config.getGuards().get(0).getDirection());
		assertEquals(Double.valueOf(10), Double.valueOf(config.getGuards().get(0).getValue()));
		assertEquals(HealthColor.GREEN, config.getGuards().get(0).getColor());
	}

	@Test
	public void testSingleLine() {
		List<String> lines = new LinkedList<>();
		lines.add("varName UP 10 GREEN");
		DataThresholdConfig config = new DataThresholdConfig();
		config.configure("varName", lines);

		assertEquals("varName", config.getGuardedVariableName());
		assertEquals("varName.THRESHOLD", config.getTargetVariableName());
	}

	@Test
	public void testManyLines() {
		List<String> lines = new LinkedList<>();
		lines.add("varName DOWN 10 GREEN anotherName");
		lines.add("varName UP 10 YELLOW anotherName");
		lines.add("varName UP 20 RED anotherName");

		DataThresholdConfig config = new DataThresholdConfig();
		config.configure("varName", lines);

		assertEquals(3, config.getGuards().size());
	}

	@Test
	public void testManyLinesWithWrongLine() {
		List<String> lines = new LinkedList<>();
		lines.add("varName DOWN 10 GREEN anotherName");
		lines.add("varNameUP 10 YELLOW anotherName");
		lines.add("varName UP 20 RED anotherName");

		DataThresholdConfig config = new DataThresholdConfig();
		config.configure("varName", lines);

		assertEquals(2, config.getGuards().size());
	}

	@Test
	public void testTargetVariableNamePrio() {
		List<String> lines = new LinkedList<>();
		lines.add("varName DOWN 10 GREEN");
		lines.add("varName UP 10 YELLOW name");

		DataThresholdConfig config = new DataThresholdConfig();
		config.configure("varName", lines);

		assertEquals("name", config.getTargetVariableName());
	}

	@Test
	public void testTargetVariableNameOverwrite() {
		List<String> lines = new LinkedList<>();
		lines.add("varName DOWN 10 GREEN");
		lines.add("varName UP 10 YELLOW name");
		lines.add("varName UP 20 RED anotherName");

		DataThresholdConfig config = new DataThresholdConfig();
		config.configure("varName", lines);

		assertEquals("anotherName", config.getTargetVariableName());
	}
}
