package org.moskito.control.common;

import net.anotheria.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

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
	private List<String> messages;

	public Status(){
		health = HealthColor.GREEN;
		messages = new ArrayList<String>();
	}

	public Status(HealthColor aColor, String aMessage){
		this();
		health = aColor;
		addMessage(aMessage);
	}

	public Status(HealthColor aColor, List<String> aMessages){
		this();
		health = aColor;
		aMessages.forEach(this::addMessage);
	}

	public HealthColor getHealth() {
		return health;
	}

	public void setHealth(HealthColor health) {
		this.health = health;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	@Override public String toString(){
		return getHealth() + " " +
				(getMessages().size() == 0 ?
				"" : getMessages().toString());
	}

	public void addMessage(String message){
		if (!StringUtils.isEmpty(message)) {
			messages.add(message);
		}
	}
}
