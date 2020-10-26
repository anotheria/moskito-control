package org.moskito.control.core;

import net.anotheria.util.StringUtils;
import org.moskito.control.common.HealthColor;
import org.moskito.control.common.Status;
import org.moskito.control.config.ComponentConfig;
import org.moskito.control.core.status.StatusChangeEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Represents a component.
 *
 * @author lrosenberg
 * @since 26.02.13 01:33
 */
public class Component implements Cloneable{
	/**
	 * Name of the component.
	 */
	private String name;

	/**
	 * Category of the component.
	 */
	private String category;

	/**
	 * Current status of the category.
	 */
	private Status status;


	/**
	 * Component tags.
	 */
	private List<String> tags = Collections.emptyList();

	/**
	 * Timestamp of the last update.
	 */
	private long lastUpdateTimestamp;

	/**
	 * Config of this component (at the moment of creation). On config reload this will be gone, not updated.
	 */
	private ComponentConfig componentConfig;

	/**
	 * If true the component was dynamically registered.
	 */
	private boolean isDynamic;

	/**
	 * Custom component attributes.
	 */
	private Map<String, String> attributes;

	/**
	 * Number of currently executed requests (if supported by the connector).
	 */
	private int currentRequestCount;


    /**
	 * Creates a new component.
	 */
	public Component(ComponentConfig config){
		status = new Status(HealthColor.NONE, "None yet");
		componentConfig = config;
		setCategory(config.getCategory());
		setName(config.getName());
		if (config.getTags()!=null && config.getTags().length()>0)
			tags = Arrays.asList(StringUtils.tokenize(config.getTags(), ','));

	}

	/**
	 * This constructor is ONLY FOR TESTING purposes.
	 * @param name
	 */
	public Component(String name){
		setName(name);
	}

	public HealthColor getHealthColor() {
		return status.getHealth();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		lastUpdateTimestamp = System.currentTimeMillis();
		Status oldStatus = this.status;
		this.status = status;
		if(oldStatus !=null && oldStatus.getHealth() != status.getHealth()){
            StatusChangeEvent event = new StatusChangeEvent(this, oldStatus, status, lastUpdateTimestamp);
			ComponentRepository.getInstance().getEventsDispatcher().addStatusChange(event);
		}
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public long getLastUpdateTimestamp(){
		return lastUpdateTimestamp;
	}

	public boolean isDynamic() {
		return isDynamic;
	}

	public void setDynamic(boolean dynamic) {
		isDynamic = dynamic;
	}

	@Override
	protected Component clone() {
		try {
			return (Component)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError("can't happen");
		}
	}

	@Override public String toString(){
		return name+"/"+category+"/"+tags;
	}

	public ComponentConfig getConfiguration() {
		return componentConfig;
	}

	public void setAttributes(Map<String,String> attributes){
		this.attributes = attributes;
	}

	public Map<String,String> getAttributes(){
		return attributes;
	}

	public int getCurrentRequestCount() {
		return currentRequestCount;
	}

	public void setCurrentRequestCount(int currentRequestCount) {
		this.currentRequestCount = currentRequestCount;
	}
}
