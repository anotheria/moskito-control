package org.moskito.controlagent.data.status;

import net.anotheria.moskito.core.threshold.ThresholdStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Container class for the threshold status and message.
 *
 * @author lrosenberg
 * @since 15.04.13 22:28
 */
public class ThresholdStatusHolder implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * Status.
	 */
	private ThresholdStatus status = ThresholdStatus.GREEN;
	/**
	 * Info about thresholds in the status.
	 */
	private List<ThresholdInfo> thresholds = new ArrayList<ThresholdInfo>();

	public ThresholdStatus getStatus() {
		return status;
	}

	public void setStatus(ThresholdStatus status) {
		this.status = status;
	}

	public List<ThresholdInfo> getThresholds() {
		return thresholds;
	}

	public void setThresholds(List<ThresholdInfo> thresholds) {
		this.thresholds = thresholds;
	}

	public void addThresholdInfo(ThresholdInfo info){
		thresholds.add(info);
	}
}
