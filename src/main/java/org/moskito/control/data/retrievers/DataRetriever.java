package org.moskito.control.data.retrievers;

import org.moskito.control.config.datarepository.VariableMapping;

import java.util.List;
import java.util.Map;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 04.06.18 14:08
 */
public interface DataRetriever {
	Map<String,String> retrieveData();

	void configure(String configurationParameter, List<VariableMapping> mappings);
}
