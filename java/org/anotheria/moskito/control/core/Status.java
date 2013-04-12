package org.anotheria.moskito.control.core;

/**
 * This class represents the status of a component.
 *
 * @author lrosenberg
 * @since 26.02.13 18:45
 */
public class Status {
	/**
	 * The health of the component.
	 */
	private HealthColor health;

	/**
	 * Components last message if applicable.
	 */
	private String message;

	public Status(){
		health = HealthColor.GREEN;
		message = null;
	}

	public HealthColor getHealth() {
		return health;
	}

	public void setHealth(HealthColor health) {
		this.health = health;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override public String toString(){
		return getHealth() + " " + getMessage();
	}
}
