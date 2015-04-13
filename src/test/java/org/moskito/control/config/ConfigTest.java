package org.moskito.control.config;

import org.moskito.control.connectors.ConnectorType;
import org.moskito.control.connectors.HttpConnector;
import org.moskito.control.connectors.NoopConnector;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test for configuration.
 *
 * @author lrosenberg
 * @since 24.04.13 11:29
 */
public class ConfigTest {
	@Test
	public void testApplicationsParsing(){
		MoskitoControlConfiguration config = MoskitoControlConfiguration.loadConfiguration();
		ApplicationConfig[] apps = config.getApplications();
		assertNotNull("Applications is null!", apps);
		assertEquals(2, apps.length);
		assertEquals("FirstApp", apps[0].getName());
		assertEquals("SecondApp", apps[1].getName());
	}

	@Test
	public void testComponentParsing(){
		MoskitoControlConfiguration config = MoskitoControlConfiguration.loadConfiguration();
		ApplicationConfig[] apps = config.getApplications();
		ComponentConfig[] components = apps[0].getComponents();

		assertEquals(2, components.length);
		ComponentConfig first = components[0];
		assertEquals("web01", first.getName());
		assertEquals("web01.google.com", first.getLocation());
		assertEquals(ConnectorType.HTTP, first.getConnectorType());
		assertEquals("web", first.getCategory());
	}

	@Test
	public void testConnectorParsing(){
		MoskitoControlConfiguration config = MoskitoControlConfiguration.loadConfiguration();
		ConnectorConfig[] connectors = config.getConnectors();
		assertNotNull("Connector shouldn't be empty", connectors);

		assertEquals(2, connectors.length);

		ConnectorConfig httpC= connectors[0];
		assertEquals(ConnectorType.HTTP, httpC.getType());
		assertEquals(HttpConnector.class.getName(), httpC.getClassName());

		ConnectorConfig noneC= connectors[1];
		assertEquals(ConnectorType.NOOP, noneC.getType());
		assertEquals(NoopConnector.class.getName(), noneC.getClassName());
	}


}

