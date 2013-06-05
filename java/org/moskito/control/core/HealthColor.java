package org.moskito.control.core;

/**
 * List of possible colors.
 *
 * @author lrosenberg
 * @since 26.02.13 18:45
 */
public enum HealthColor {
	/**
	 * Green.
	 */
	GREEN,
	/**
	 * Yellow.
	 */
	YELLOW,
	/**
	 * Orange.
	 */
	ORANGE,
	/**
	 * Red.
	 */
	RED,
	/**
	 * Purple.
	 */
	PURPLE,
	/**
	 * None yet.
	 */
	NONE;

	/**
	 * Returns true if my status is worse than the parameter color.
	 * @param anotherColor colour to compare to.
	 * @return
	 */
	public boolean isWorse(HealthColor anotherColor){
		return anotherColor!=null && anotherColor.ordinal() < ordinal();
	}
}
