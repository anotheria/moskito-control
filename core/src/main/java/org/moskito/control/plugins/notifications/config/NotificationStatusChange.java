package org.moskito.control.plugins.notifications.config;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.control.common.HealthColor;

import java.util.Arrays;

/**
 * Configuration for change of colours.
 *
 * @author lrosenberg
 * @since 08.05.17 16:56
 */
@ConfigureMe
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2", "EI_EXPOSE_REP"},
		justification = "This is the way configureMe works, it provides beans for access")
public class NotificationStatusChange {
	/**
	 * Colours of new statuses.
	 */
	@Configure private String[] newColors;
	/**
	 * Colours of the old statuses.
	 */
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

	/**
	 * Returns true if the new/old colour of an event matches this configured. 
	 * @param newHealthColor
	 * @param oldHealthColor
	 * @return true - colors matches this status change criteria
	 *         false - no
	 */
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
