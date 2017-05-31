package org.moskito.control.plugins.notifications.config;

import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.core.HealthColor;

import java.util.Arrays;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 08.05.17 16:56
 */
@ConfigureMe
public class NotificationStatusChange {

	@Configure private String[] newColors;
	@Configure private String[] oldColors;

	/**
	 * Constructor for testing.
	 * @param newColors
	 * @param oldColors
	 */
	public NotificationStatusChange(String[] newColors, String[] oldColors) {
		this.newColors = newColors;
		this.oldColors = oldColors;
	}

	/**
	 * Default constructor used by ConfigureMe
	 */
	public NotificationStatusChange() {
	}

	public String[] getNewColors() {
		return newColors;
	}

	public void setNewColors(String[] newColors) {
		this.newColors = newColors;
	}

	public String[] getOldColors() {
		return oldColors;
	}

	public void setOldColors(String[] oldColors) {
		this.oldColors = oldColors;
	}

	@Override
	public String toString() {
		return "NotificationStatusChange{" +
				"newColor=" + Arrays.toString(newColors) +
				", oldColor=" + Arrays.toString(oldColors) +
				'}';
	}

	public boolean isAppliableToEvent(HealthColor newHealthColor, HealthColor oldHealthColor) {
		return doesColorMatch(newHealthColor.name(), newColors) &&
				doesColorMatch(oldHealthColor.name(), oldColors);
	}

	private boolean doesColorMatch(String toMatch, String[] toMatchWith){
		if (toMatchWith==null || toMatchWith.length==0)
			return true;
		for (String s : toMatchWith){
			if (s.equals(toMatch))
				return true;
		}
		return false;

	}
}
