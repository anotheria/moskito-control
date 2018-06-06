package org.moskito.control.data.processors;

import java.util.Map;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 04.06.18 16:13
 */
public interface DataProcessor {
	Map<String, String> process(Map<String, String> data);

	void configure(String variable, String parameter);
}
