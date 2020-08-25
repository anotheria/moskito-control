package org.moskito.control.ui.bean;

import org.moskito.control.common.HealthColor;

/**
 * This class cumulates components by health color.
 *
 * @author lrosenberg
 * @since 02.04.13 00:10
 */
public class ComponentCountByHealthStatusBean {
	/**
	 * Number of green components.
	 */
	private StatusStatisticsBean green;
	/**
	 * Number of yellow components.
	 */
	private StatusStatisticsBean yellow;
	/**
	 * Number of orange components. Warning, orange may be excluded from view.
	 */
	private StatusStatisticsBean orange;
	/**
	 * Number of red components.
	 */
	private StatusStatisticsBean red;
	/**
	 * Number of purple components.
	 */
	private StatusStatisticsBean purple;

	/**
	 * Number of components without status yet.
	 */
	private StatusStatisticsBean none;


	public ComponentCountByHealthStatusBean() {
		green = new StatusStatisticsBean("green");
		yellow = new StatusStatisticsBean("yellow");
		orange = new StatusStatisticsBean("orange");
		red = new StatusStatisticsBean("red");
		purple = new StatusStatisticsBean("purple");
		none = new StatusStatisticsBean("none");
	}


	public StatusStatisticsBean getGreen() {
		return green;
	}

	public void setGreen(StatusStatisticsBean green) {
		this.green = green;
	}

	public StatusStatisticsBean getYellow() {
		return yellow;
	}

	public void setYellow(StatusStatisticsBean yellow) {
		this.yellow = yellow;
	}

	public StatusStatisticsBean getOrange() {
		return orange;
	}

	public void setOrange(StatusStatisticsBean orange) {
		this.orange = orange;
	}

	public StatusStatisticsBean getRed() {
		return red;
	}

	public void setRed(StatusStatisticsBean red) {
		this.red = red;
	}

	public StatusStatisticsBean getPurple() {
		return purple;
	}

	public void setPurple(StatusStatisticsBean purple) {
		this.purple = purple;
	}

	public StatusStatisticsBean getNone() {
		return none;
	}

	public void setNone(StatusStatisticsBean none) {
		this.none = none;
	}

	/**
	 * Adds a new components color to the statistics.
	 *
	 * @param color color to count.
	 */
	public void addColor(HealthColor color) {
		getByColor(color).addComponent();
	}

	public void setSelected(HealthColor color) {
		getByColor(color).setSelected(true);
	}

	private StatusStatisticsBean getByColor(HealthColor color) {
		switch (color) {
			case GREEN:
				return green;
			case YELLOW:
				return yellow;
			case ORANGE:
				return orange;
			case RED:
				return red;
			case PURPLE:
				return purple;
			case NONE:
				return none;
			default:
				throw new IllegalArgumentException("Unknown color " + color);
		}
	}
}