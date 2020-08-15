package org.moskito.control.plugins.slack;

import org.junit.Test;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.notifications.config.NotificationStatusChange;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 05.05.17 16:46
 */
public class SlackChannelConfigTest {

	@Test
	public void testApplicableWithoutStatus(){
		SlackChannelConfig config = new SlackChannelConfig();
		assertTrue(config.isAppliableToEvent(createEvent( HealthColor.GREEN)));
	}

	@Test
	public void testApplicableIfMatchedStatus(){
		SlackChannelConfig config = new SlackChannelConfig();
		config.setNotificationStatusChanges(
			new NotificationStatusChange[]{
					new NotificationStatusChange(
							new String[]{"RED", "ORANGE"},
							null)
			});
		assertTrue(config.isAppliableToEvent(createEvent( HealthColor.RED)));
	}

	@Test
	public void testNonApplicableIfNotMatchedStatus(){
		SlackChannelConfig config = new SlackChannelConfig();
		config.setNotificationStatusChanges(
				new NotificationStatusChange[]{
						new NotificationStatusChange(
								new String[]{"RED", "YELLOW"},
								null)
				});
		assertFalse(config.isAppliableToEvent(createEvent( HealthColor.GREEN)));
	}

	private StatusChangeEvent createEvent(HealthColor color){
		StatusChangeEvent event = new StatusChangeEvent();
		event.setStatus(new Status(color, ""));
		event.setOldStatus(new Status(HealthColor.GREEN, ""));
		return event;
	}
}
