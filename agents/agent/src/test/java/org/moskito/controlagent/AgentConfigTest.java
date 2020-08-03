package org.moskito.controlagent;

import org.configureme.ConfigurationManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AgentConfigTest{
	@Test public void testEmptyConfig(){
		AgentConfig config = new AgentConfig();
		config.afterConfiguration();
		assertTrue(config.includeAll());
		assertEquals(0, config.getIncludedProducersList().size());
		assertEquals(0, config.getExcludedProducersList().size());

	}

	@Test public void testFileConfig(){
		AgentConfig config = new AgentConfig();
		ConfigurationManager.INSTANCE.configure(config);
		assertFalse(config.includeAll());

		assertTrue(config.getIncludedProducersList().contains("Foo"));
		assertEquals(3, config.getIncludedProducersList().size());

		assertFalse(config.getExcludedProducersList().contains("Foo"));
		assertEquals(1, config.getExcludedProducersList().size());
	}


}