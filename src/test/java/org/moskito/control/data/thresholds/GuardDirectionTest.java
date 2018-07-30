package org.moskito.control.data.thresholds;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class GuardDirectionTest {
	@Test
	public void forNameTest() {
		String[] ups = {"up", "above", "higher", "upper", "over", "more"};
		for (String direction : ups) {
			assertEquals(GuardDirection.forName(direction), GuardDirection.UP);
			assertEquals(GuardDirection.forName(direction.toUpperCase()), GuardDirection.UP);
		}

		String[] downs = {"down", "below", "lower", "under", "less"};
		for (String direction : downs) {
			assertEquals(GuardDirection.forName(direction), GuardDirection.DOWN);
			assertEquals(GuardDirection.forName(direction.toLowerCase()), GuardDirection.DOWN);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void forNameWrongNameTest() {
		GuardDirection.forName("nonexistent");
	}
}
