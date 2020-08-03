package org.moskito.control.config;

import org.junit.Ignore;
import org.junit.Test;
import org.moskito.control.config.datarepository.DataProcessingConfig;
import org.moskito.control.config.datarepository.WidgetConfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for configuration.
 *
 * @author lrosenberg
 * @since 24.04.13 11:29
 */
public class ConfigTest {
	@Test
	public void testComponentParsing(){
		MoskitoControlConfiguration config = MoskitoControlConfiguration.loadConfiguration();
		ComponentConfig[] components = config.getComponents();

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
		assertEquals("org.moskito.control.connectors.HttpConnector", httpC.getClassName());

		ConnectorConfig noneC= connectors[1];
		assertEquals(ConnectorType.NOOP, noneC.getType());
		assertEquals("org.moskito.control.connectors.NoopConnector", noneC.getClassName());
	}

	/**
	 * Tests getting components names for lines using
	 * component name pattern in config
	 */
	@Test
	public void testChartsParsing(){
		   //TODO REVISIT
		/**
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

		 */


	}

	@Test
	public void testMultipleComponentsCaption(){
		 //TODO revisit
		/*
		MoskitoControlConfiguration config = MoskitoControlConfiguration.loadConfiguration();
		ApplicationConfig testApp = config.getApplication("FirstApp");

		ChartConfig testChart = testApp.getCharts()[0];

		ChartLineConfig hasCaptionConfig = testChart.getLines()[0];
		ChartLineConfig noCaptionConfig = testChart.getLines()[4];

		String firstHasCaptionComponentName = hasCaptionConfig.getComponentsMatcher().getMatchedComponents(testApp.getComponents())[0];
		String firstNoCaptionComponentName = noCaptionConfig.getComponentsMatcher().getMatchedComponents(testApp.getComponents())[0];

		assertEquals("web2 categories components (web03)", hasCaptionConfig.getCaption(firstHasCaptionComponentName));
		assertNull(noCaptionConfig.getCaption(firstNoCaptionComponentName));
		*/
	}

	@Test @Ignore
	public void testDataWidgetsParsing(){
		//TODO revisit
		/*
		MoskitoControlConfiguration config = MoskitoControlConfiguration.loadConfiguration();
		ApplicationConfig testApp = config.getApplication("FirstApp");

		assertNotNull("DataWidgets is null!", testApp.getDataWidgets());
		assertTrue(testApp.getDataWidgets().length > 0);
		*/
	}

	@Test
	public void testPrimitiveValuesConfig(){
		MoskitoControlConfiguration config = MoskitoControlConfiguration.loadConfiguration();
		assertEquals(111, config.getHistoryItemsAmount());
		assertEquals(11, config.getNotificationsMutingTime());
		assertEquals(false, config.isTrackUsage());
	}

	@Test
	public void testStatusUpdaterParser(){
		MoskitoControlConfiguration config = MoskitoControlConfiguration.loadConfiguration();
		UpdaterConfig su = config.getStatusUpdater();
		assertEquals(11, su.getCheckPeriodInSeconds());
		assertEquals(12, su.getThreadPoolSize());
		assertEquals(13, su.getTimeoutInSeconds());
		assertEquals(false, su.isEnabled());
	}

	@Test
	public void testChartsUpdaterParser(){
		MoskitoControlConfiguration config = MoskitoControlConfiguration.loadConfiguration();
		UpdaterConfig cu = config.getChartsUpdater();
		assertEquals(22, cu.getCheckPeriodInSeconds());
		assertEquals(23, cu.getThreadPoolSize());
		assertEquals(24, cu.getTimeoutInSeconds());
		assertEquals(false, cu.isEnabled());
	}

	@Test
	public void testPluginsConfigParsing(){
		MoskitoControlConfiguration config = MoskitoControlConfiguration.loadConfiguration();
		assertNotNull("PluginsConfig is null!", config.getPluginsConfig());

		PluginConfig[] plugins = config.getPluginsConfig().getPlugins();
		assertNotNull("Plugins is null!", plugins);
		assertTrue(plugins.length > 0);

		PluginConfig plugin = plugins[0];
		assertEquals("plugin0_name", plugin.getName());
		assertEquals("net.anotheria.moskito.core.plugins.NoOpPlugin", plugin.getClassName());
		assertEquals("plugin0_configurationName", plugin.getConfigurationName());
	}

	@Test
	public void testDataprocessingParsing(){
		MoskitoControlConfiguration config = MoskitoControlConfiguration.loadConfiguration();
		DataProcessingConfig dpc = config.getDataprocessing();
		assertTrue(dpc.getPreprocessing().length > 0);
		assertEquals("processing", dpc.getProcessing()[0]);

		assertTrue(dpc.getPreprocessing().length > 0);
		assertEquals("preprocessing", dpc.getPreprocessing()[0]);

		assertTrue(dpc.getWidgets().length > 0);

		WidgetConfig wc = dpc.getWidgets()[0];
		assertEquals("widget0_type", wc.getType());
		assertEquals("widget0_caption", wc.getCaption());
		assertEquals("widget0_mapping=mapping", wc.getMapping());
		assertEquals("widget0_name", wc.getName());
	}
}

