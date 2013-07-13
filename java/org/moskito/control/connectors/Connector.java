package org.moskito.control.connectors;

import java.util.List;

/**
 * Connector
 *
 * @author lrosenberg
 * @since 26.02.13 18:44
 */
public interface Connector {
	void configure(String location);

	ConnectorStatusResponse getNewStatus();

	ConnectorAccumulatorResponse getAccumulators(List<String> accumulatorNames);
}
