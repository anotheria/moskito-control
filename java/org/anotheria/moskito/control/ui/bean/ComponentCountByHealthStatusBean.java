package org.anotheria.moskito.control.ui.bean;

import org.anotheria.moskito.control.core.HealthColor;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 02.04.13 00:10
 */
public class ComponentCountByHealthStatusBean {
	private int green;
	private int yellow;
	private int orange;
	private int red;
	private int purple;

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
			default:
				throw new IllegalArgumentException("Unknown color "+color);
		}
	}
}
