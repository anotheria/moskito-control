package org.moskito.controlagent.data.nowrunning;

import net.anotheria.moskito.webui.nowrunning.api.MeasurementAO;

import java.io.Serializable;

/**
 * This class mirrors net.anotheria.moskito.webui.nowrunning.api.MeasurementAO
 * to remove breaking upon change and to use in both rmi and http endpoints.
 *
 * @author lrosenberg
 * @since 15.10.20 13:53
 */
public class Measurement implements Serializable {

	private static final long serialVersionUID = 1L;

	private long starttime;
	private long age;
	private String description;
	private long endtime;
	private long duration;

	public Measurement(){

	}

	public Measurement(MeasurementAO ao){
		starttime = ao.getStarttime();
		age = ao.getAge();
		description = ao.getDescription();
		endtime = ao.getEndtime();
		duration = ao.getDuration();
	}

	public long getStarttime() {
		return starttime;
	}

	public void setStarttime(long starttime) {
		this.starttime = starttime;
	}

	public long getAge() {
		return age;
	}

	public void setAge(long age) {
		this.age = age;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getEndtime() {
		return endtime;
	}

	public void setEndtime(long endtime) {
		this.endtime = endtime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
}
