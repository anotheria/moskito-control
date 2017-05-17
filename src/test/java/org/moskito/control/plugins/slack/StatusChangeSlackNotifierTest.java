package org.moskito.control.plugins.slack;

import org.junit.Test;
import org.moskito.control.core.Application;
import org.moskito.control.core.Component;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.status.Status;
import org.moskito.control.core.status.StatusChangeEvent;

import static org.junit.Assert.assertEquals;
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
	@Test
	public void testNoLinkReplacement(){
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
	@Test
	public void testAllColoursAreHandled(){
		for (HealthColor c : HealthColor.values()){
			String response = StatusChangeSlackNotifier.color2color(c);
			assertNotNull(response);
			assertTrue(response.length()>0);
		}
	}

	private StatusChangeEvent createStatusChangeEvent(){
		Application app = new Application("TestAPP");
		StatusChangeEvent event = new StatusChangeEvent(
				app,
				new Component(app, "TestComp"),
				new Status(HealthColor.RED, "RED"),
				new Status(HealthColor.GREEN, "GREEN"),
				System.currentTimeMillis()

		);

		return event;
	}

}
