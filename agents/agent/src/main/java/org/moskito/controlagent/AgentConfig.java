package org.moskito.controlagent;

import net.anotheria.util.StringUtils;
import org.configureme.annotations.AbortedConfiguration;
import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Configuration of the agent.
 *
 * @author lrosenberg
 * @since 15.04.13 20:33
 */
@ConfigureMe(name="agent")
public class AgentConfig {

	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(AgentConfig.class);

	/**
	 * Included producer names, comma separated or '*'.
	 */
	@Configure
	private String includedProducers;

	/**
	 * Excluded producer names, comma separated.
	 */
	@Configure
	private String excludedProducers;

	/**
	 * Post-processed list with included producers.
	 */
	private List<String> includedProducersList;
	/**
	 * Post-processed list with excluded producers.
	 */
	private List<String> excludedProducersList;

	public String getIncludedProducers() {
		return includedProducers;
	}

	public void setIncludedProducers(String includedProducers) {
		this.includedProducers = includedProducers;
	}

	public String getExcludedProducers() {
		return excludedProducers;
	}

	public void setExcludedProducers(String excludedProducers) {
		this.excludedProducers = excludedProducers;
	}

	@AfterConfiguration @AbortedConfiguration
	public void afterConfiguration(){
		if (includedProducers==null || includedProducers.length()==0 || includedProducers.trim().equals("*")){
			includedProducersList = Collections.EMPTY_LIST;
		}else{
			String tt[] = StringUtils.tokenize(includedProducers, ',');
			for (int i=0; i<tt.length; i++)
				tt[i] = tt[i].trim();
			includedProducersList = Arrays.asList(tt);
		}
		if (excludedProducers==null || excludedProducers.length()==0){
			excludedProducersList = Collections.EMPTY_LIST;
		}else{
			String tt[] = StringUtils.tokenize(excludedProducers, ',');
			for (int i=0; i<tt.length; i++)
				tt[i] = tt[i].trim();
			excludedProducersList = Arrays.asList(tt);
		}
		log.info("AgentConfig parsed and finished, includeAll: "+includeAll()+", inclList: "+includedProducersList+", exclList: "+excludedProducersList);
	}

	public List<String> getIncludedProducersList(){
		return includedProducersList;
	}

	public List<String> getExcludedProducersList(){
		return excludedProducersList;
	}

	/**
	 * Returns true if all producers should be included.
	 * @return
	 */
	public boolean includeAll(){
		return includedProducersList.size()==0 && excludedProducersList.size() == 0;
	}

}
