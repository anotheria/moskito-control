package org.moskito.control.plugins.slack;

import org.configureme.ConfigurationManager;
import org.junit.Ignore;
import org.junit.Test;
import org.moskito.control.core.Component;
import org.moskito.control.core.HealthColor;
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
	 * This test ensures that if a new color is added to the HealthColors, the developer will add a mapping for the slack integration as well.
	 */
	@Test
	public void testAllColoursAreHandled(){
		for (HealthColor c : HealthColor.values()){
			String response = SlackMessageBuilder.color2color(c);
			assertNotNull(response);
			assertTrue(response.length()>0);
		}
	}

	private StatusChangeEvent createStatusChangeEvent(String appName){
		StatusChangeEvent event = new StatusChangeEvent(
				
				new Component("TestComp"),
				new Status(HealthColor.RED, "RED"),
				new Status(HealthColor.GREEN, "GREEN"),
				System.currentTimeMillis()

		);

		return event;
	}

	//Since there are no applications anymore we don't need tests.
	@Ignore
	@Test public void testRoutingIntoTestChannelOnly(){
		StatusChangeEvent event = createStatusChangeEvent("TEST");
		SlackConfig config = new SlackConfig();
		ConfigurationManager.INSTANCE.configureAs(config, "slack");

		assertEquals("test-monitoring", config.getProfileForEvent(event).get(0).getName());
	}

	//Since there are no applications anymore we don't need tests.
	@Ignore
	@Test public void testRoutingIntoFooChannelOnlyWithStatus(){
		StatusChangeEvent event = createStatusChangeEvent("FOO");
		event.setStatus(new Status(HealthColor.RED, ""));
		event.setOldStatus(new Status(HealthColor.GREEN, ""));
		SlackConfig config = new SlackConfig();
		ConfigurationManager.INSTANCE.configureAs(config, "slack");

		//not in channel
		assertEquals(0, config.getProfileForEvent(event).size());

		StatusChangeEvent event2 = createStatusChangeEvent("FOO");
		event2.setStatus(new Status(HealthColor.PURPLE, ""));


		//not in channel
		assertEquals("foo-monitoring", config.getProfileForEvent(event2).get(0).getName());

	}
	//ROUTING is disabled since we don't have applications anymore.
	@Test @Ignore
	public void testRoutingIntoMultipleChannels(){
		StatusChangeEvent event = createStatusChangeEvent("PROD");
		event.setStatus(new Status(HealthColor.PURPLE, ""));
		event.setOldStatus(new Status(HealthColor.GREEN, ""));
		SlackConfig config = new SlackConfig();
		ConfigurationManager.INSTANCE.configureAs(config, "slack");

		assertEquals(2, config.getProfileForEvent(event).size());
		//ensure not in default channel
		assertFalse(
		        config.getProfileForEvent(event)
                        .stream()
                        .anyMatch(channel -> channel.getName().equals("general"))
        );

		//ensure not in foo channel
		assertFalse(
                config.getProfileForEvent(event)
                        .stream()
                        .anyMatch(channel -> channel.getName().equals("foo-monitoring"))
        );

		//ensure in prod and test channels.
		assertTrue(
                config.getProfileForEvent(event)
                        .stream()
                        .anyMatch(channel -> channel.getName().equals("test-monitoring"))
        );

		assertTrue(
                config.getProfileForEvent(event)
                        .stream()
                        .anyMatch(channel -> channel.getName().equals("prod-monitoring"))
        );


		StatusChangeEvent event2 = createStatusChangeEvent("FOO");
		event2.setStatus(new Status(HealthColor.PURPLE, ""));

		//not in channel
		assertEquals("foo-monitoring", config.getProfileForEvent(event2).get(0).getName());

	}

	@Test public void testConditionsWithOldAndNewStatus(){

		StatusChangeEvent event = createStatusChangeEvent("YELLOWTEST");
		event.setStatus(new Status(HealthColor.PURPLE, ""));
		event.setOldStatus(new Status(HealthColor.GREEN, ""));

		SlackConfig config = new SlackConfig();
		ConfigurationManager.INSTANCE.configureAs(config, "slack");

		//ensure not in foo channel
		assertFalse(
		        config.getProfileForEvent(event)
                .stream()
                .anyMatch(channel -> channel.getName().equals("only-yellow-monitoring"))
        );

		//and now with new yellow, but old orange
		event.setOldStatus(new Status(HealthColor.ORANGE, ""));
		event.setStatus(new Status(HealthColor.YELLOW, ""));

		//ensure not in foo channel
		assertFalse(
                config.getProfileForEvent(event)
                        .stream()
                        .anyMatch(channel -> channel.getName().equals("only-yellow-monitoring"))
        );

		//and now with new yellow, and old green
		event.setOldStatus(new Status(HealthColor.GREEN, ""));
		event.setStatus(new Status(HealthColor.YELLOW, ""));

		//ensure not in foo channel
		assertTrue(
                config.getProfileForEvent(event)
                        .stream()
                        .anyMatch(channel -> channel.getName().equals("only-yellow-monitoring"))
        );

	}


}
