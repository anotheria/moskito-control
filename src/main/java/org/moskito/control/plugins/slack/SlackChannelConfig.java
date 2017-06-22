package org.moskito.control.plugins.slack;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.plugins.notifications.config.BaseNotificationProfileConfig;
import org.moskito.control.plugins.notifications.config.NotificationStatusChange;

import java.util.Arrays;

/**
 * Configuration for single Slack channel
 * Links channel with applications
 */
@ConfigureMe
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2", "EI_EXPOSE_REP"},
        justification = "This is the way configureMe works, it provides beans for access")
public class SlackChannelConfig extends BaseNotificationProfileConfig{

    /**
     * Name of Slack channel
     */
    @Configure
    private String name;

    /**
     * Applications names linked to this channel
     */
    @Configure
    private String[] applications;

    @Configure
    private NotificationStatusChange[] notificationStatusChanges = new NotificationStatusChange[0];

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getApplications() {
        return applications;
    }

    public void setApplications(String[] applications) {
        this.applications = applications;
    }

	@Override
	public String toString() {
		return "SlackChannelConfig{" +
				"name='" + name + '\'' +
				", applications=" + Arrays.toString(applications) +
				", notificationStatusChanges=" + Arrays.toString(notificationStatusChanges) +
				'}';
	}

	public NotificationStatusChange[] getStatusChanges() {
		return notificationStatusChanges;
	}

	public void setNotificationStatusChanges(NotificationStatusChange[] notificationStatusChanges) {
		this.notificationStatusChanges = notificationStatusChanges;
	}
}
