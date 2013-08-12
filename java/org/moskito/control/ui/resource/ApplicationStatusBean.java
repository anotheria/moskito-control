package org.moskito.control.ui.resource;

import net.anotheria.util.NumberUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.08.13 18:23
 */
@XmlRootElement
public class ApplicationStatusBean {
	@XmlElement
	private long lastStatusUpdaterRun;
	@XmlElement
	private long lastStatusUpdaterSuccess;
	@XmlElement
	private long lastChartUpdaterRun;
	@XmlElement
	private long lastChartUpdaterSuccess;



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

	@XmlElement
	public String getLastChartUpdaterRunAsString(){
		return getTimestampAsString(lastChartUpdaterRun);
	}
	@XmlElement
	public String getLastChartUpdaterSuccessAsString(){
		return getTimestampAsString(lastChartUpdaterSuccess);
	}
	@XmlElement
	public String setLastStatusUpdaterRunAsString(){
		return getTimestampAsString(lastStatusUpdaterRun);
	}
	@XmlElement
	public String getLastStatusUpdaterSuccessAsString(){
		return getTimestampAsString(lastStatusUpdaterSuccess);
	}

	private String getTimestampAsString(long timestamp){
		return timestamp == 0 ? "Never" : NumberUtils.makeISO8601TimestampString(timestamp);
	}
}
