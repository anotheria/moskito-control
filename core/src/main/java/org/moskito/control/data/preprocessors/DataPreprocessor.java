package org.moskito.control.data.preprocessors;

import java.util.Map;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 07.06.18 14:29
 */
public interface DataPreprocessor {
	Map<String, String> process(Map<String, String> oldData);

	void configure(String variable, String parameter);

}
