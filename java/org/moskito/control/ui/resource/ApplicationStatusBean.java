package org.moskito.control.ui.resource;

import net.anotheria.util.NumberUtils;
import org.moskito.control.core.HealthColor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.08.13 18:23
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ApplicationStatusBean {
	@XmlElement
	private long lastStatusUpdaterRun;
	@XmlElement
	private long lastStatusUpdaterSuccess;
	@XmlElement
	private long lastChartUpdaterRun;
	@XmlElement
	private long lastChartUpdaterSuccess;
	@XmlElement
	private HealthColor color;
	@XmlElement
	private long statusUpdaterRunCount;
	@XmlElement
	private long statusUpdaterSuccessCount;
	@XmlElement
	private long chartUpdaterSuccessCount;
	@XmlElement
	private long chartUpdaterRunCount;




	public long getLastStatusUpdaterRun() {
		return lastStatusUpdaterRun;
	}

	public void setLastStatusUpdaterRun(long lastStatusUpdaterRun) {
		this.lastStatusUpdaterRun = lastStatusUpdaterRun;
	}

	public long getLastStatusUpdaterSuccess() {
		return lastStatusUpdaterSuccess;
	}

	public void setLastStatusUpdaterSuccess(long lastStatusUpdaterSuccess) {
		this.lastStatusUpdaterSuccess = lastStatusUpdaterSuccess;
	}

	public long getLastChartUpdaterRun() {
		return lastChartUpdaterRun;
	}

	public void setLastChartUpdaterRun(long lastChartUpdaterRun) {
		this.lastChartUpdaterRun = lastChartUpdaterRun;
	}

	public long getLastChartUpdaterSuccess() {
		return lastChartUpdaterSuccess;
	}

	public void setLastChartUpdaterSuccess(long lastChartUpdaterSuccess) {
		this.lastChartUpdaterSuccess = lastChartUpdaterSuccess;
	}

	@XmlElement(name="lastChartUpdaterRunString")
	public String getLastChartUpdaterRunAsString(){
		return getTimestampAsString(lastChartUpdaterRun);
	}

	@XmlElement(name="lastChartUpdaterSuccessString")
	public String getLastChartUpdaterSuccessAsString(){
		return getTimestampAsString(lastChartUpdaterSuccess);
	}

	@XmlElement(name="lastStatusUpdaterRunString")
	public String setLastStatusUpdaterRunAsString(){
		return getTimestampAsString(lastStatusUpdaterRun);
	}

	@XmlElement(name="lastStatusUpdaterSuccessString")
	public String getLastStatusUpdaterSuccessAsString(){
		return getTimestampAsString(lastStatusUpdaterSuccess);
	}

	private String getTimestampAsString(long timestamp){
		return timestamp == 0 ? "Never" : NumberUtils.makeISO8601TimestampString(timestamp);
	}

	public void setColor(HealthColor color) {
		this.color = color;
	}

	public HealthColor getColor() {
		return color;
	}

	public long getStatusUpdaterRunCount() {
		return statusUpdaterRunCount;
	}

	public void setStatusUpdaterRunCount(long statusUpdaterRunCount) {
		this.statusUpdaterRunCount = statusUpdaterRunCount;
	}

	public long getStatusUpdaterSuccessCount() {
		return statusUpdaterSuccessCount;
	}

	public void setStatusUpdaterSuccessCount(long statusUpdaterSuccessCount) {
		this.statusUpdaterSuccessCount = statusUpdaterSuccessCount;
	}

	public long getChartUpdaterSuccessCount() {
		return chartUpdaterSuccessCount;
	}

	public void setChartUpdaterSuccessCount(long chartUpdaterSuccessCount) {
		this.chartUpdaterSuccessCount = chartUpdaterSuccessCount;
	}

	public long getChartUpdaterRunCount() {
		return chartUpdaterRunCount;
	}

	public void setChartUpdaterRunCount(long chartUpdaterRunCount) {
		this.chartUpdaterRunCount = chartUpdaterRunCount;
	}
}
