package org.moskito.controlagent.data.nowrunning;

import net.anotheria.moskito.webui.nowrunning.api.EntryPointAO;
import net.anotheria.moskito.webui.nowrunning.api.MeasurementAO;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Mirrors net.anotheria.moskito.webui.nowrunning.api.EntryPointAO for class change de-coupling.
 *
 * @author lrosenberg
 * @since 15.10.20 13:55
 */
public class EntryPoint implements Serializable {

	private static final long serialVersionUID = 1L;

	private String producerId;
	private long currentRequestCount;
	private long totalRequestCount;
	private List<Measurement> currentMeasurements;
	private List<Measurement> pastMeasurements;

	public EntryPoint(){}

	public EntryPoint(EntryPointAO ao){
		producerId = ao.getProducerId();
		currentRequestCount = ao.getCurrentRequestCount();
		totalRequestCount = ao.getTotalRequestCount();
		currentMeasurements = copyList(ao.getCurrentMeasurements());
		pastMeasurements    = copyList(ao.getPastMeasurements());

	}

	private List<Measurement> copyList(List<MeasurementAO> src){
		if (src==null || src.size()==0)
			return Collections.emptyList();
		LinkedList<Measurement> dest = new LinkedList<>();
		for (MeasurementAO ao: src){
			dest.add(new Measurement(ao));
		}
		return dest;
	}

	public String getProducerId() {
		return producerId;
	}

	public void setProducerId(String producerId) {
		this.producerId = producerId;
	}

	public long getCurrentRequestCount() {
		return currentRequestCount;
	}

	public void setCurrentRequestCount(long currentRequestCount) {
		this.currentRequestCount = currentRequestCount;
	}

	public long getTotalRequestCount() {
		return totalRequestCount;
	}

	public void setTotalRequestCount(long totalRequestCount) {
		this.totalRequestCount = totalRequestCount;
	}

	public List<Measurement> getCurrentMeasurements() {
		return currentMeasurements;
	}

	public void setCurrentMeasurements(List<Measurement> currentMeasurements) {
		this.currentMeasurements = currentMeasurements;
	}

	public List<Measurement> getPastMeasurements() {
		return pastMeasurements;
	}

	public void setPastMeasurements(List<Measurement> pastMeasurements) {
		this.pastMeasurements = pastMeasurements;
	}

	@Override
	public String toString() {
		return "EntryPoint{" +
				"producerId='" + producerId + '\'' +
				", currentRequestCount=" + currentRequestCount +
				", totalRequestCount=" + totalRequestCount +
				", currentMeasurements=" + currentMeasurements +
				", pastMeasurements=" + pastMeasurements +
				'}';
	}
}
