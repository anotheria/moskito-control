package org.anotheria.moskito.control.ui.bean;

import org.anotheria.moskito.control.core.HealthColor;

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
	private int green;
	/**
	 * Number of yellow components.
	 */
	private int yellow;
	/**
	 * Number of orange components. Warning, orange may be excluded from view.
	 */
	private int orange;
	/**
	 * Number of red components.
	 */
	private int red;
	/**
	 * Number of purple components.
	 */
	private int purple;

	/**
	 * Number of components without status yet.
	 */
	private int none;

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getYellow() {
		return yellow;
	}

	public void setYellow(int yellow) {
		this.yellow = yellow;
	}

	public int getOrange() {
		return orange;
	}

	public void setOrange(int orange) {
		this.orange = orange;
	}

	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getPurple() {
		return purple;
	}

	public void setPurple(int purple) {
		this.purple = purple;
	}

	public int getNone() {
		return none;
	}

	public void setNone(int none) {
		this.none = none;
	}

	/**
	 * Adds a new components color to the statistics.
	 * @param color color to count.
	 */
	public void addColor(HealthColor color){
		switch(color){
			case GREEN:
				green++; break;
			case YELLOW:
				yellow++; break;
			case ORANGE:
				orange++; break;
			case RED:
				red++; break;
			case PURPLE:
				purple++; break;
			case NONE:
				none++; break;
			default:
				throw new IllegalArgumentException("Unknown color "+color);
		}
	}
}
