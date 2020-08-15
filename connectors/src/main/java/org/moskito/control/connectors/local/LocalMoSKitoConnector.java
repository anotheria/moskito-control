package org.moskito.control.connectors.local;

import net.anotheria.moskito.core.accumulation.Accumulator;
import net.anotheria.moskito.core.accumulation.AccumulatorRepository;
import net.anotheria.moskito.core.threshold.Threshold;
import net.anotheria.moskito.core.threshold.ThresholdRepository;
import net.anotheria.moskito.core.threshold.ThresholdStatus;
import org.moskito.control.common.AccumulatorDataItem;
import org.moskito.control.common.HealthColor;
import org.moskito.control.common.Status;
import org.moskito.control.connectors.AbstractConnector;
import org.moskito.control.connectors.response.ConnectorAccumulatorResponse;
import org.moskito.control.connectors.response.ConnectorAccumulatorsNamesResponse;
import org.moskito.control.connectors.response.ConnectorInformationResponse;
import org.moskito.control.connectors.response.ConnectorStatusResponse;
import org.moskito.control.connectors.response.ConnectorThresholdsResponse;
import org.moskito.controlagent.Agent;
import org.moskito.controlagent.data.accumulator.AccumulatorHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 13.08.20 22:43
 */
public class LocalMoSKitoConnector extends AbstractConnector {

	private String componentName;

	private static Logger log = LoggerFactory.getLogger(LocalMoSKitoConnector.class);

	@Override
	public void configure(String componentName, String location, String credentials) {
		this.componentName = componentName;
	}

	@Override
	public ConnectorStatusResponse getNewStatus() {
		//step1, find worst status of the producer.
		ThresholdRepository repository = ThresholdRepository.getInstance();
		List<Threshold> thresholds = repository.getThresholds();
		ThresholdStatus myComponentsStatus = ThresholdStatus.GREEN;
		for (Threshold t : thresholds){
			if (!t.getDefinition().getProducerName().equals(componentName))
				continue;
			if (t.getStatus().overrules(myComponentsStatus)) {
				myComponentsStatus = t.getStatus();
			}
		}

		Status status = new Status();
		status.setHealth(HealthColor.getHealthColor(myComponentsStatus));
		List<String> messages = new LinkedList<>();
		//step2 combine messages
		for (Threshold t : thresholds){
			if (!t.getDefinition().getProducerName().equals(componentName))
				continue;
			if (t.getStatus()==myComponentsStatus){
				messages.add(t.getName()+" "+t.getLastValue());
			}
		}
		status.setMessages(messages);
		return new ConnectorStatusResponse(status);
	}

	@Override
	public ConnectorThresholdsResponse getThresholds() {
		List<org.moskito.control.common.ThresholdDataItem> controlThresholds = new ArrayList<>();
		ThresholdRepository repository = ThresholdRepository.getInstance();
		List<Threshold> thresholds = repository.getThresholds();
		for (Threshold t : thresholds){
			if (t.getDefinition().getProducerName().equals(componentName)){
				org.moskito.control.common.ThresholdDataItem item = new org.moskito.control.common.ThresholdDataItem();
				item.setName(t.getName());
				item.setLastValue(t.getLastValue());
				item.setStatus(HealthColor.getHealthColor(t.getStatus()));
				item.setStatusChangeTimestamp(t.getStatusChangeTimestamp());
				controlThresholds.add(item);
			}
		}
		return new ConnectorThresholdsResponse(controlThresholds);
	}

	@Override
	public ConnectorAccumulatorResponse getAccumulators(List<String> accumulatorNames) {
		ConnectorAccumulatorResponse response = new ConnectorAccumulatorResponse();

		Map<String, AccumulatorHolder> accumulatorHolders = Agent.getInstance().getAccumulatorsData(accumulatorNames);
		for (String name : accumulatorNames){
			AccumulatorHolder holder = accumulatorHolders.get(name);
			if (holder == null){
				log.warn("Accumulator "+name+" is missing (is null) in response from "+componentName);
				continue;
			}
			LinkedList<AccumulatorDataItem> dataLine = new LinkedList<AccumulatorDataItem>();
			for (org.moskito.controlagent.data.accumulator.AccumulatorDataItem agentItem : holder.getItems()){
				dataLine.add(agent2control(agentItem));
			}
			response.addDataLine(name, dataLine);

		}

		return response;
	}



	@Override
	public ConnectorAccumulatorsNamesResponse getAccumulatorsNames() throws IOException {

		LinkedList<String> names = new LinkedList<>();
		List<Accumulator> allAccumulators = AccumulatorRepository.getInstance().getAccumulators();
		for (Accumulator acc : allAccumulators){
			if (acc.getDefinition().getProducerName().equals(componentName))
				names.add(acc.getName());
		}

		return new ConnectorAccumulatorsNamesResponse(names);
	}

	@Override
	public ConnectorInformationResponse getInfo() {

		return null;
	}

	@Override
	public boolean supportsInfo() {
		return super.supportsInfo();
	}

	@Override
	public boolean supportsThresholds() {
		return true;
	}

	@Override
	public boolean supportsAccumulators() {
		return true;
	}
}
