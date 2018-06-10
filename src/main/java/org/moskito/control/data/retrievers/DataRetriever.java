package org.moskito.control.data.retrievers;

import org.moskito.control.config.datarepository.VariableMapping;

import java.util.List;
import java.util.Map;

/**
 * Data retriever to retrieve dynamic data.
 *
 * @author lrosenberg
 * @since 04.06.18 14:08
 */
public interface DataRetriever {
	/**
	 * Returns retrieved data.
	 * @return
	 */
	Map<String,String> retrieveData();

	/**
	 * Called from the configuration process in DataRepository.
	 * @param configurationParameter a configuration parameter, typically a url.
	 * @param mappings a list of mapping between expressions and target variables.
	 */
	void configure(String configurationParameter, List<VariableMapping> mappings);
}
