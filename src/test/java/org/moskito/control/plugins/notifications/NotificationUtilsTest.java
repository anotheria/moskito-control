package org.moskito.control.plugins.notifications;

import org.junit.Test;
import org.moskito.control.core.Application;
import org.moskito.control.core.Component;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.status.Status;
import org.moskito.control.core.status.StatusChangeEvent;

import static org.junit.Assert.assertEquals;

public class NotificationUtilsTest {

    private StatusChangeEvent createStatusChangeEvent(){

        Application app = new Application("TestAPP");

        return new StatusChangeEvent(
                app,
                new Component(app, "TestComp"),
                new Status(HealthColor.RED, "RED"),
                new Status(HealthColor.GREEN, "GREEN"),
                System.currentTimeMillis()

        );

    }

    /**
     * Test that the link is unmodified if it doesn't contain custom parameters.
     */
    @Test
    public void testBuildAlertLinkNoReplacement(){

        String link = "http://domain:port/app/";

        String targetLink = NotificationUtils.buildAlertLink(link, createStatusChangeEvent());
        assertEquals(link,  targetLink);

    }

    /**
     * Test if application is set properly in the link
     */
    @Test
    public void testLinkReplacement(){

        String link = "http://domain:port/app/action?application=${APPLICATION}";

        String targetLink = NotificationUtils.buildAlertLink(link, createStatusChangeEvent());
        assertEquals("http://domain:port/app/action?application=TestAPP", targetLink);

    }

}
