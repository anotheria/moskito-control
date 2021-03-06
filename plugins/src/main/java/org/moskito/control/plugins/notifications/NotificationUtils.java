package org.moskito.control.plugins.notifications;

import org.moskito.control.common.HealthColor;
import org.moskito.control.core.status.StatusChangeEvent;

/**
 * Utility methods for notification plugins
 */
public class NotificationUtils {


    /**
     * Returns link to thumb image for inserting
     * it to notification messages
     * @param basePath base path to images
     * @param color status color
     * @return link to thumb image correspond to color
     */
    public static String getThumbImageUrlByColor(String basePath, HealthColor color){
        return basePath + color.name().toLowerCase() + ".png";
    }

    /**
     * Builds link to application, that will be placed to notification message.
     * @param event current event
     * @param linkTemplate template for link to applications
     * @return url leads to application, noted in status change event
     */
    public static String buildAlertLink(String linkTemplate, StatusChangeEvent event){
    	//TODO previously we supported things like application name, maybe we should change the whole
		//notification logic here.
    	return linkTemplate;
    }

}
