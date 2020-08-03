package org.moskito.controlagent.endpoints.rmi;

import org.moskito.controlagent.Agent;
import org.moskito.controlagent.data.accumulator.AccumulatorHolder;
import org.moskito.controlagent.data.accumulator.AccumulatorListItem;
import org.moskito.controlagent.data.info.SystemInfo;
import org.moskito.controlagent.data.info.SystemInfoProvider;
import org.moskito.controlagent.data.status.ThresholdStatusHolder;
import org.moskito.controlagent.data.threshold.ThresholdDataItem;

import java.util.List;
import java.util.Map;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 09.04.14 13:50
 */
public class AgentServiceImpl implements AgentService {
	@Override
	public SystemInfo getSystemInfo() throws AgentServiceException {
		return SystemInfoProvider.getInstance().getSystemInfo();
	}

	@Override
	public ThresholdStatusHolder getThresholdStatus() throws AgentServiceException {
		return Agent.getInstance().getThresholdStatus();
	}

	@Override
	public List<ThresholdDataItem> getThresholds() throws AgentServiceException {
		return Agent.getInstance().getThresholds();
	}

	@Override
	public List<AccumulatorListItem> getAvailableAccumulators() throws AgentServiceException {
		return Agent.getInstance().getAvailableAccumulators();
	}

	@Override
	public Map<String, AccumulatorHolder> getAccumulatorsData(List<String> accumulatorNames) throws AgentServiceException {
		return Agent.getInstance().getAccumulatorsData(accumulatorNames);
	}
}
