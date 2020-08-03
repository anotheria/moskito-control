package org.moskito.control.data.processors;

import java.util.Map;

/**
 * Interface that defines a data processor. DataProcessors are used to transform previously retrieved data with help of mathematic or string operations.
 *
 * @author lrosenberg
 * @since 04.06.18 16:13
 */
public interface DataProcessor {
	/**
	 * Calculation results returned as a map.
	 * @param data the incoming data, previously preprocessed, retrieved and processed by other processors.
	 * @return
	 */
	Map<String, String> process(Map<String, String> data);

	/**
	 * Called upon initialization.
	 * @param variable targetVariableName.
	 * @param parameter processor specific parameters, usually comma separated.
	 */
	void configure(String variable, String parameter);
}
