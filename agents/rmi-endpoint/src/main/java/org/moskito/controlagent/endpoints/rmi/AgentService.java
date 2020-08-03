package org.moskito.controlagent.endpoints.rmi;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;
import org.moskito.controlagent.data.accumulator.AccumulatorHolder;
import org.moskito.controlagent.data.accumulator.AccumulatorListItem;
import org.moskito.controlagent.data.info.SystemInfo;
import org.moskito.controlagent.data.status.ThresholdStatusHolder;
import org.moskito.controlagent.data.threshold.ThresholdDataItem;

import java.util.List;
import java.util.Map;

/**
 * AgentService is the base for distribution and a distributeme wrapper for the Agent.
 *
 * @author lrosenberg
 * @since 09.04.14 13:50
 */
@DistributeMe(moskitoSupport = false, agentsSupport = false)
public interface AgentService extends Service{

	/**
	 * Returns info about this app and its environment
	 * @return app info object
	 * @throws AgentServiceException on
	 */
	SystemInfo getSystemInfo() throws AgentServiceException;

	/**
	 * Returns the current status.
	 * @return
	 * @throws AgentServiceException
	 */
	ThresholdStatusHolder getThresholdStatus() throws AgentServiceException;

	/**
	 * Returns the list of contained thresholds.
	 * @return
	 * @throws AgentServiceException
	 */
	List<ThresholdDataItem> getThresholds() throws AgentServiceException;

	/**
	 * Returns the list of available accumulators.
	 * @return
	 * @throws AgentServiceException
	 */
	List<AccumulatorListItem> getAvailableAccumulators() throws AgentServiceException;

	/**
	 * Returns accumulator data
	 * @param accumulatorNames
	 * @return
	 * @throws AgentServiceException
	 */
	Map<String, AccumulatorHolder> getAccumulatorsData(List<String> accumulatorNames) throws AgentServiceException;

}
