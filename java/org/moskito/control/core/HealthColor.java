package org.moskito.control.core;

import net.anotheria.moskito.core.threshold.ThresholdStatus;

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

    /**
     * Returns appropriate threshold color by {@link ThresholdStatus} value.
     * Remains for having single color set through MoSKito Control.
     *
     * @see org.moskito.control.connectors.parsers.V1Parser ;
     * @param status {@link ThresholdStatus}.
     * @return {@link ThresholdStatus}.
     */
    public static HealthColor getHealthColor(ThresholdStatus status){
        switch (status) {
            case OFF:
                return HealthColor.NONE;
            case GREEN:
                return HealthColor.GREEN;
            case YELLOW:
                return HealthColor.YELLOW;
            case ORANGE:
                return HealthColor.ORANGE;
            case RED:
                return HealthColor.RED;
            case PURPLE:
                return HealthColor.PURPLE;
            default:
                return null;
        }
    }

}
