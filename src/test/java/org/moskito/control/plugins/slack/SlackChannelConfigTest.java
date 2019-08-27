package org.moskito.control.plugins.slack;

import org.junit.Test;
import org.moskito.control.core.Application;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.status.Status;
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

	private static final Application PROD = new Application("PROD");

	@Test
	public void testApplicableIfSameAppNameWithoutStatus(){
		SlackChannelConfig config = new SlackChannelConfig();
		config.setApplications(new String[]{"PROD"});
		assertTrue(config.isAppliableToEvent(createEvent( HealthColor.GREEN)));
	}

	@Test
	public void testNotApplicableAnotherAppNameWithoutStatus(){
		SlackChannelConfig config = new SlackChannelConfig();
		config.setApplications(new String[]{"FOO"});
		assertFalse(config.isAppliableToEvent(createEvent( HealthColor.GREEN)));
	}

	@Test
	public void testApplicableIfSameAppNameAndMatchedStatus(){
		SlackChannelConfig config = new SlackChannelConfig();
		config.setApplications(new String[]{"PROD"});
		config.setNotificationStatusChanges(
			new NotificationStatusChange[]{
					new NotificationStatusChange(
							new String[]{"RED", "ORANGE"},
							null)
			});
		assertTrue(config.isAppliableToEvent(createEvent( HealthColor.RED)));
	}

	@Test
	public void testNonApplicableIfSameAppNameAndNotMatchedStatus(){
		SlackChannelConfig config = new SlackChannelConfig();
		config.setApplications(new String[]{"PROD"});
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
