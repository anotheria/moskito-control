package org.moskito.control.plugins.pagespeed;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 05.08.20 17:13
 */
public class PagespeedConfigTest {

	public static final String TEST_CONFIG_NAME = "test-pagespeedconfig";

	@Test public void testAPIKey(){
		PagespeedPluginConfig config = PagespeedPluginConfig.getByName(TEST_CONFIG_NAME);
		assertEquals("Test-ApiKey", config.getApiKey());
	}

	@Test public void testTargets(){
		PagespeedPluginConfig config = PagespeedPluginConfig.getByName(TEST_CONFIG_NAME);
		assertNotNull("Target list shouldn't be null", config.getTargets());
		assertEquals(2, config.getTargets().length);
		PagespeedPluginTargetConfig target1 = config.getTargets()[0];
		PagespeedPluginTargetConfig target2 = config.getTargets()[1];

		assertEquals("https://www.google.com", target1.getUrl());
		assertEquals("desktop", target1.getStrategy());

		assertEquals("https://www.moskito.org", target2.getUrl());
		assertEquals("mobile", target2.getStrategy());

	}
}
