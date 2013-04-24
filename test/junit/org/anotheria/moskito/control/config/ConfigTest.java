package org.anotheria.moskito.control.config;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 24.04.13 11:29
 */
public class ConfigTest {
	@Test
	public void testApplicationsParsing(){
		MoskitoControlConfiguration config = MoskitoControlConfiguration.loadConfiguration();
		ApplicationConfig[] apps = config.getApplications();
		assertNotNull(apps);
		assertEquals(2, apps.length);
		assertEquals("FirstApp", apps[0].getName());
		assertEquals("SecondApp", apps[1].getName());
	}
}
