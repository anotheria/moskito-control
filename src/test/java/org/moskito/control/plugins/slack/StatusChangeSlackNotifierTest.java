package org.moskito.control.plugins.slack;

import org.configureme.ConfigurationManager;
import org.junit.Test;
import org.moskito.control.core.Application;
import org.moskito.control.core.Component;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.status.Status;
import org.moskito.control.core.status.StatusChangeEvent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 03.05.17 13:59
 */
public class StatusChangeSlackNotifierTest {
	/**
	 * Test that the link is unmodified if it doesn't contain custom parameters.
	 */
	@Test public void testNoLinkReplacement(){
		SlackConfig config = new SlackConfig();
		String link = "http://domain:port/app/";
		config.setAlertLink(link);
		StatusChangeSlackNotifier notifier = new StatusChangeSlackNotifier(config);

		String targetLink = notifier.buildAlertLink(createStatusChangeEvent());
		assertEquals(link,  targetLink);
	}

	/**
	 * Test if application is set properly in the link
	 */
	@Test public void testLinkReplacement(){
		SlackConfig config = new SlackConfig();
		String link = "http://domain:port/app/action?application=${APPLICATION}";
		config.setAlertLink(link);
		StatusChangeSlackNotifier notifier = new StatusChangeSlackNotifier(config);

		String targetLink = notifier.buildAlertLink(createStatusChangeEvent());
		assertEquals("http://domain:port/app/action?application=TestAPP", targetLink);

	}

	/**
	 * This test ensures that if a new color is added to the HealthColors, the developer will add a mapping for the slack integration as well.
	 */
	@Test public void testAllColoursAreHandled(){
		for (HealthColor c : HealthColor.values()){
			String response = StatusChangeSlackNotifier.color2color(c);
			assertNotNull(response);
			assertTrue(response.length()>0);
		}
	}

	private StatusChangeEvent createStatusChangeEvent(){
		return createStatusChangeEvent("TestAPP");
	}
	private StatusChangeEvent createStatusChangeEvent(String appName){
		Application app = new Application(appName);
		StatusChangeEvent event = new StatusChangeEvent(
				app,
				new Component(app, "TestComp"),
				new Status(HealthColor.RED, "RED"),
				new Status(HealthColor.GREEN, "GREEN"),
				System.currentTimeMillis()

		);

		return event;
	}

	@Test public void testRoutingIntoTestChannelOnly(){
		StatusChangeEvent event = createStatusChangeEvent("TEST");
		SlackConfig config = new SlackConfig();
		ConfigurationManager.INSTANCE.configureAs(config, "slack");

		assertEquals("test-monitoring", config.getChannelNameForEvent(event).get(0));
	}

	@Test public void testRoutingIntoFooChannelOnlyWithStatus(){
		StatusChangeEvent event = createStatusChangeEvent("FOO");
		event.setStatus(new Status(HealthColor.RED, ""));
		event.setOldStatus(new Status(HealthColor.GREEN, ""));
		SlackConfig config = new SlackConfig();
		ConfigurationManager.INSTANCE.configureAs(config, "slack");

		//not in channel
		assertEquals("general", config.getChannelNameForEvent(event).get(0));

		StatusChangeEvent event2 = createStatusChangeEvent("FOO");
		event2.setStatus(new Status(HealthColor.PURPLE, ""));


		//not in channel
		assertEquals("foo-monitoring", config.getChannelNameForEvent(event2).get(0));

	}
	@Test public void testRoutingIntoMultipleChannels(){
		StatusChangeEvent event = createStatusChangeEvent("PROD");
		event.setStatus(new Status(HealthColor.PURPLE, ""));
		event.setOldStatus(new Status(HealthColor.GREEN, ""));
		SlackConfig config = new SlackConfig();
		ConfigurationManager.INSTANCE.configureAs(config, "slack");

		assertEquals(2, config.getChannelNameForEvent(event).size());
		//ensure not in default channel
		assertFalse(config.getChannelNameForEvent(event).contains("general"));
		//ensure not in foo channel
		assertFalse(config.getChannelNameForEvent(event).contains("foo-monitoring"));

		//ensure in prod and test channels.
		assertTrue(config.getChannelNameForEvent(event).contains("test-monitoring"));
		assertTrue(config.getChannelNameForEvent(event).contains("prod-monitoring"));


		StatusChangeEvent event2 = createStatusChangeEvent("FOO");
		event2.setStatus(new Status(HealthColor.PURPLE, ""));

		//not in channel
		assertEquals("foo-monitoring", config.getChannelNameForEvent(event2).get(0));

	}

	@Test public void testConditionsWithOldAndNewStatus(){
		StatusChangeEvent event = createStatusChangeEvent("YELLOWTEST");
		event.setStatus(new Status(HealthColor.PURPLE, ""));
		event.setOldStatus(new Status(HealthColor.GREEN, ""));

		SlackConfig config = new SlackConfig();
		ConfigurationManager.INSTANCE.configureAs(config, "slack");

		//this one shouldn't fire (GREEN-PURPLE)
		assertTrue(config.getChannelNameForEvent(event).contains("general"));
		//ensure not in foo channel
		assertFalse(config.getChannelNameForEvent(event).contains("only-yellow-monitoring"));


		//and now with new yellow, but old orange
		event.setOldStatus(new Status(HealthColor.ORANGE, ""));
		event.setStatus(new Status(HealthColor.YELLOW, ""));
		//this one shouldn't fire (ORANGE-YELLOW)
		assertTrue(config.getChannelNameForEvent(event).contains("general"));
		//ensure not in foo channel
		assertFalse(config.getChannelNameForEvent(event).contains("only-yellow-monitoring"));

		//and now with new yellow, and old green
		event.setOldStatus(new Status(HealthColor.GREEN, ""));
		event.setStatus(new Status(HealthColor.YELLOW, ""));
		//this one shouldn't fire (ORANGE-YELLOW)
		assertFalse(config.getChannelNameForEvent(event).contains("general"));
		//ensure not in foo channel
		assertTrue(config.getChannelNameForEvent(event).contains("only-yellow-monitoring"));

	}


}
