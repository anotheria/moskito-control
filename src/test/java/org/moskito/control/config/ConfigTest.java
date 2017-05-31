package org.moskito.control.config;

import org.moskito.control.connectors.ConnectorType;
import org.moskito.control.connectors.HttpConnector;
import org.moskito.control.connectors.NoopConnector;
import org.junit.Test;
import org.moskito.control.core.Application;

import java.util.Arrays;

import static org.junit.Assert.*;

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

		assertEquals(4, components.length);
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

	/**
	 * Tests getting components names for lines using
	 * component name pattern in config
	 */
	@Test
	public void testChartsParsing(){

		MoskitoControlConfiguration config = MoskitoControlConfiguration.loadConfiguration();
		ApplicationConfig testApp = config.getApplication("FirstApp");

		ChartConfig testChart = testApp.getCharts()[0];

		ChartLineConfig web2CategoryComponentsChartLine = testChart.getLines()[0];
		ChartLineConfig web01ComponentLine = testChart.getLines()[1];
		ChartLineConfig anyComponentLine = testChart.getLines()[2];
		ChartLineConfig webCategoryEndsWith2Line = testChart.getLines()[3];

		assertArrayEquals(web2CategoryComponentsChartLine.getComponentsMatcher().getMatchedComponents(testApp.getComponents()),
				new String[]{"web03", "web04"});

		assertArrayEquals(web01ComponentLine.getComponentsMatcher().getMatchedComponents(testApp.getComponents()),
				new String[]{"web01"});

		assertArrayEquals(anyComponentLine.getComponentsMatcher().getMatchedComponents(testApp.getComponents()),
				new String[]{"web01", "web02", "web03", "web04"});

		assertArrayEquals(webCategoryEndsWith2Line.getComponentsMatcher().getMatchedComponents(testApp.getComponents()),
				new String[]{"web02"});


	}

	@Test
	public void testMultipleComponentsCaption(){

		MoskitoControlConfiguration config = MoskitoControlConfiguration.loadConfiguration();
		ApplicationConfig testApp = config.getApplication("FirstApp");

		ChartConfig testChart = testApp.getCharts()[0];

		ChartLineConfig hasCaptionConfig = testChart.getLines()[0];
		ChartLineConfig noCaptionConfig = testChart.getLines()[4];

		String firstHasCaptionComponentName = hasCaptionConfig.getComponentsMatcher().getMatchedComponents(testApp.getComponents())[0];
		String firstNoCaptionComponentName = noCaptionConfig.getComponentsMatcher().getMatchedComponents(testApp.getComponents())[0];

		assertEquals("web2 categories components (web03)", hasCaptionConfig.getCaption(firstHasCaptionComponentName));
		assertNull(noCaptionConfig.getCaption(firstNoCaptionComponentName));


	}


}

