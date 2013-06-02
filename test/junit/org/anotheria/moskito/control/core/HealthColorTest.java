package org.anotheria.moskito.control.core;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class HealthColorTest{
	@Test public void testIsWorse(){
		assertTrue(HealthColor.RED.isWorse(HealthColor.ORANGE));
		assertTrue(HealthColor.ORANGE.isWorse(HealthColor.YELLOW));
		assertTrue(HealthColor.YELLOW.isWorse(HealthColor.GREEN));
		assertTrue(HealthColor.PURPLE.isWorse(HealthColor.RED));
		assertTrue(HealthColor.PURPLE.isWorse(HealthColor.GREEN));
	}
}